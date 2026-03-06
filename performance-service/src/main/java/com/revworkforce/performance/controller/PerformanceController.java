package com.revworkforce.performance.controller;

import com.revworkforce.performance.dto.PerformanceReviewRequest;
import com.revworkforce.performance.dto.PerformanceReviewResponse;
import com.revworkforce.performance.dto.ReviewFeedbackRequest;
import com.revworkforce.performance.dto.TeamReviewResponse;
import com.revworkforce.performance.service.PerformanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    private final PerformanceService performanceService;

    @Autowired
    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @PostMapping
    public ResponseEntity<PerformanceReviewResponse> createReview(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody PerformanceReviewRequest request) {
        PerformanceReviewResponse response = performanceService.createReview(userId, request, role);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/my")
    public ResponseEntity<List<PerformanceReviewResponse>> getMyReviews(
            @RequestHeader("X-User-Id") Long userId) {
        List<PerformanceReviewResponse> reviews = performanceService.getMyReviews(userId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/team")
    public ResponseEntity<TeamReviewResponse> getTeamReviews(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {
        TeamReviewResponse response = performanceService.getTeamReviews(userId, role);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/submit")
    public ResponseEntity<PerformanceReviewResponse> submitReview(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id) {
        PerformanceReviewResponse response = performanceService.submitReview(userId, id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/feedback")
    public ResponseEntity<PerformanceReviewResponse> provideFeedback(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id,
            @Valid @RequestBody ReviewFeedbackRequest request) {
        PerformanceReviewResponse response = performanceService.provideFeedback(userId, id, request, role);
        return ResponseEntity.ok(response);
    }
}
