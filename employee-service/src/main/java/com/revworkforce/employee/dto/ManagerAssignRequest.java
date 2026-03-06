package com.revworkforce.employee.dto;

import jakarta.validation.constraints.NotNull;

public class ManagerAssignRequest {

    @NotNull(message = "Manager ID is required")
    private Long managerId;

    // Constructors
    public ManagerAssignRequest() {
    }

    private ManagerAssignRequest(Builder builder) {
        this.managerId = builder.managerId;
    }

    // Getters
    public Long getManagerId() {
        return managerId;
    }

    // Setters
    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long managerId;

        public Builder managerId(Long managerId) {
            this.managerId = managerId;
            return this;
        }

        public ManagerAssignRequest build() {
            return new ManagerAssignRequest(this);
        }
    }
}
