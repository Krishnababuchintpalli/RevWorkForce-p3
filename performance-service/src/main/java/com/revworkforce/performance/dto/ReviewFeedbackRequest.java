package com.revworkforce.performance.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewFeedbackRequest {

    @NotBlank(message = "Manager feedback is required")
    @Size(max = 2000, message = "Manager feedback must not exceed 2000 characters")
    private String managerFeedback;

    @NotNull(message = "Manager rating is required")
    @Min(value = 1, message = "Manager rating must be between 1 and 5")
    @Max(value = 5, message = "Manager rating must be between 1 and 5")
    private Integer managerRating;

    public ReviewFeedbackRequest() {
    }

    public String getManagerFeedback() {
        return managerFeedback;
    }

    public void setManagerFeedback(String managerFeedback) {
        this.managerFeedback = managerFeedback;
    }

    public Integer getManagerRating() {
        return managerRating;
    }

    public void setManagerRating(Integer managerRating) {
        this.managerRating = managerRating;
    }
}
