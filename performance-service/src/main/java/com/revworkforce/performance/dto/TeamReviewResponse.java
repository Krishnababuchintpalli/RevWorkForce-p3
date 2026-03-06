package com.revworkforce.performance.dto;

import java.util.List;

public class TeamReviewResponse {

    private List<PerformanceReviewResponse> reviews;

    public TeamReviewResponse() {
    }

    public TeamReviewResponse(List<PerformanceReviewResponse> reviews) {
        this.reviews = reviews;
    }

    public List<PerformanceReviewResponse> getReviews() {
        return reviews;
    }

    public void setReviews(List<PerformanceReviewResponse> reviews) {
        this.reviews = reviews;
    }
}
