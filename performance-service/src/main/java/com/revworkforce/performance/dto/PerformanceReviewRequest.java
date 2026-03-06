package com.revworkforce.performance.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PerformanceReviewRequest {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @Size(max = 2000, message = "Key deliverables must not exceed 2000 characters")
    private String keyDeliverables;

    @Size(max = 2000, message = "Accomplishments must not exceed 2000 characters")
    private String accomplishments;

    @Size(max = 2000, message = "Areas of improvement must not exceed 2000 characters")
    private String areasOfImprovement;

    @Min(value = 1, message = "Self rating must be between 1 and 5")
    @Max(value = 5, message = "Self rating must be between 1 and 5")
    private Integer selfRating;

    public PerformanceReviewRequest() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getKeyDeliverables() {
        return keyDeliverables;
    }

    public void setKeyDeliverables(String keyDeliverables) {
        this.keyDeliverables = keyDeliverables;
    }

    public String getAccomplishments() {
        return accomplishments;
    }

    public void setAccomplishments(String accomplishments) {
        this.accomplishments = accomplishments;
    }

    public String getAreasOfImprovement() {
        return areasOfImprovement;
    }

    public void setAreasOfImprovement(String areasOfImprovement) {
        this.areasOfImprovement = areasOfImprovement;
    }

    public Integer getSelfRating() {
        return selfRating;
    }

    public void setSelfRating(Integer selfRating) {
        this.selfRating = selfRating;
    }
}
