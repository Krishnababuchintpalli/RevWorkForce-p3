package com.revworkforce.performance.service;

import com.revworkforce.performance.client.EmployeeServiceClient;
import com.revworkforce.performance.dto.EmployeeDto;
import com.revworkforce.performance.dto.PerformanceReviewRequest;
import com.revworkforce.performance.dto.PerformanceReviewResponse;
import com.revworkforce.performance.dto.ReviewFeedbackRequest;
import com.revworkforce.performance.dto.TeamReviewResponse;
import com.revworkforce.performance.entity.PerformanceReview;
import com.revworkforce.performance.enums.ReviewStatus;
import com.revworkforce.performance.exception.ResourceNotFoundException;
import com.revworkforce.performance.exception.UnauthorizedException;
import com.revworkforce.performance.repository.PerformanceReviewRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PerformanceService {

    private final PerformanceReviewRepository reviewRepository;
    private final EmployeeServiceClient employeeServiceClient;
    private final PerformanceNotificationService notificationService;

    @Autowired
    public PerformanceService(PerformanceReviewRepository reviewRepository,
                            EmployeeServiceClient employeeServiceClient,
                            PerformanceNotificationService notificationService) {
        this.reviewRepository = reviewRepository;
        this.employeeServiceClient = employeeServiceClient;
        this.notificationService = notificationService;
    }

    @CircuitBreaker(name = "employee-service", fallbackMethod = "createReviewFallback")
    public PerformanceReviewResponse createReview(Long reviewerId, PerformanceReviewRequest request, String role) {
        // Only manager/admin can create reviews for employees
        if (!role.equals("MANAGER") && !role.equals("ADMIN")) {
            throw new UnauthorizedException("Only managers and admins can create performance reviews");
        }

        // Verify employee exists
        EmployeeDto employee = employeeServiceClient.getEmployee(request.getEmployeeId());
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId());
        }

        PerformanceReview review = new PerformanceReview();
        review.setEmployeeId(request.getEmployeeId());
        review.setReviewerId(reviewerId);
        review.setKeyDeliverables(request.getKeyDeliverables());
        review.setAccomplishments(request.getAccomplishments());
        review.setAreasOfImprovement(request.getAreasOfImprovement());
        review.setSelfRating(request.getSelfRating());
        // Support manager/admin creating review with feedback directly
        review.setManagerFeedback(request.getManagerFeedback());
        review.setManagerRating(request.getManagerRating());
        // If manager feedback provided, mark as REVIEWED; otherwise DRAFT
        if (request.getManagerFeedback() != null || request.getManagerRating() != null) {
            review.setStatus(ReviewStatus.REVIEWED);
        } else {
            review.setStatus(ReviewStatus.DRAFT);
        }

        PerformanceReview savedReview = reviewRepository.save(review);

        // Notify employee
        notificationService.notifyReviewCreated(savedReview);

        return mapToResponse(savedReview);
    }

    @CircuitBreaker(name = "employee-service", fallbackMethod = "getMyReviewsFallback")
    public List<PerformanceReviewResponse> getMyReviews(Long userId) {
        // Get employee by userId
        EmployeeDto employee = employeeServiceClient.getEmployeeByUserId(userId);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found for user id: " + userId);
        }

        List<PerformanceReview> reviews = reviewRepository.findByEmployeeId(employee.getId());
        return reviews.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "employee-service", fallbackMethod = "getTeamReviewsFallback")
    public TeamReviewResponse getTeamReviews(Long userId, String role) {
        // Only manager/admin can view team reviews
        if (!role.equals("MANAGER") && !role.equals("ADMIN")) {
            throw new UnauthorizedException("Only managers and admins can view team reviews");
        }

        // Get manager's employee record
        EmployeeDto manager = employeeServiceClient.getEmployeeByUserId(userId);
        if (manager == null) {
            throw new ResourceNotFoundException("Employee not found for user id: " + userId);
        }

        // For ADMIN, get all reviews; for MANAGER, get their team's reviews
        List<PerformanceReview> reviews;
        if (role.equals("ADMIN")) {
            reviews = reviewRepository.findAll();
        } else {
            reviews = reviewRepository.findByReviewerId(manager.getId());
        }

        List<PerformanceReviewResponse> responses = reviews.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new TeamReviewResponse(responses);
    }

    public PerformanceReviewResponse submitReview(Long userId, Long reviewId) {
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        // Verify this is the employee's own review
        EmployeeDto employee = employeeServiceClient.getEmployeeByUserId(userId);
        if (employee == null || !employee.getId().equals(review.getEmployeeId())) {
            throw new UnauthorizedException("You can only submit your own reviews");
        }

        if (review.getStatus() != ReviewStatus.DRAFT) {
            throw new IllegalStateException("Only draft reviews can be submitted");
        }

        review.setStatus(ReviewStatus.SUBMITTED);
        PerformanceReview updatedReview = reviewRepository.save(review);

        return mapToResponse(updatedReview);
    }

    @CircuitBreaker(name = "employee-service", fallbackMethod = "provideFeedbackFallback")
    public PerformanceReviewResponse provideFeedback(Long userId, Long reviewId, ReviewFeedbackRequest request, String role) {
        // Only manager/admin can provide feedback
        if (!role.equals("MANAGER") && !role.equals("ADMIN")) {
            throw new UnauthorizedException("Only managers and admins can provide feedback");
        }

        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        // Verify the review has been submitted
        if (review.getStatus() != ReviewStatus.SUBMITTED) {
            throw new IllegalStateException("Feedback can only be provided on submitted reviews");
        }

        review.setManagerFeedback(request.getManagerFeedback());
        review.setManagerRating(request.getManagerRating());
        review.setStatus(ReviewStatus.REVIEWED);

        PerformanceReview updatedReview = reviewRepository.save(review);

        // Notify employee
        notificationService.notifyFeedbackProvided(updatedReview);

        return mapToResponse(updatedReview);
    }

    @CircuitBreaker(name = "employee-service", fallbackMethod = "mapToResponseFallback")
    private PerformanceReviewResponse mapToResponse(PerformanceReview review) {
        PerformanceReviewResponse response = new PerformanceReviewResponse();
        response.setId(review.getId());
        response.setEmployeeId(review.getEmployeeId());
        response.setReviewerId(review.getReviewerId());
        response.setKeyDeliverables(review.getKeyDeliverables());
        response.setAccomplishments(review.getAccomplishments());
        response.setAreasOfImprovement(review.getAreasOfImprovement());
        response.setSelfRating(review.getSelfRating());
        response.setManagerFeedback(review.getManagerFeedback());
        response.setManagerRating(review.getManagerRating());
        response.setStatus(review.getStatus());
        response.setCreatedAt(review.getCreatedAt());
        response.setUpdatedAt(review.getUpdatedAt());

        // Fetch employee and reviewer names
        try {
            EmployeeDto employee = employeeServiceClient.getEmployee(review.getEmployeeId());
            if (employee != null) {
                response.setEmployeeName(employee.getFullName());
            }

            EmployeeDto reviewer = employeeServiceClient.getEmployee(review.getReviewerId());
            if (reviewer != null) {
                response.setReviewerName(reviewer.getFullName());
            }
        } catch (Exception e) {
            // If employee service is down, still return the response without names
            response.setEmployeeName("Unknown");
            response.setReviewerName("Unknown");
        }

        return response;
    }

    // Fallback methods
    private PerformanceReviewResponse createReviewFallback(Long reviewerId, PerformanceReviewRequest request, String role, Exception ex) {
        throw new RuntimeException("Employee service is currently unavailable. Please try again later.");
    }

    private List<PerformanceReviewResponse> getMyReviewsFallback(Long userId, Exception ex) {
        throw new RuntimeException("Employee service is currently unavailable. Please try again later.");
    }

    private TeamReviewResponse getTeamReviewsFallback(Long userId, String role, Exception ex) {
        throw new RuntimeException("Employee service is currently unavailable. Please try again later.");
    }

    private PerformanceReviewResponse provideFeedbackFallback(Long userId, Long reviewId, ReviewFeedbackRequest request, String role, Exception ex) {
        throw new RuntimeException("Employee service is currently unavailable. Please try again later.");
    }

    private PerformanceReviewResponse mapToResponseFallback(PerformanceReview review, Exception ex) {
        PerformanceReviewResponse response = new PerformanceReviewResponse();
        response.setId(review.getId());
        response.setEmployeeId(review.getEmployeeId());
        response.setReviewerId(review.getReviewerId());
        response.setKeyDeliverables(review.getKeyDeliverables());
        response.setAccomplishments(review.getAccomplishments());
        response.setAreasOfImprovement(review.getAreasOfImprovement());
        response.setSelfRating(review.getSelfRating());
        response.setManagerFeedback(review.getManagerFeedback());
        response.setManagerRating(review.getManagerRating());
        response.setStatus(review.getStatus());
        response.setCreatedAt(review.getCreatedAt());
        response.setUpdatedAt(review.getUpdatedAt());
        response.setEmployeeName("Unknown");
        response.setReviewerName("Unknown");
        return response;
    }
}
