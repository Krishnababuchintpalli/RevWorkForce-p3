package com.revworkforce.employee.dto;

public class EmployeeSummaryResponse {

    private Long id;
    private String fullName;
    private String email;
    private Long managerId;

    // Constructors
    public EmployeeSummaryResponse() {
    }

    private EmployeeSummaryResponse(Builder builder) {
        this.id = builder.id;
        this.fullName = builder.fullName;
        this.email = builder.email;
        this.managerId = builder.managerId;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public Long getManagerId() {
        return managerId;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String fullName;
        private String email;
        private Long managerId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder managerId(Long managerId) {
            this.managerId = managerId;
            return this;
        }

        public EmployeeSummaryResponse build() {
            return new EmployeeSummaryResponse(this);
        }
    }
}
