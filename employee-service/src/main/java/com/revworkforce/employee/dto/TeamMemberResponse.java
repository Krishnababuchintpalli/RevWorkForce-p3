package com.revworkforce.employee.dto;

public class TeamMemberResponse {

    private Long id;
    private String employeeId;
    private String fullName;
    private String email;
    private String designation;
    private String status;

    // Constructors
    public TeamMemberResponse() {
    }

    private TeamMemberResponse(Builder builder) {
        this.id = builder.id;
        this.employeeId = builder.employeeId;
        this.fullName = builder.fullName;
        this.email = builder.email;
        this.designation = builder.designation;
        this.status = builder.status;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getDesignation() {
        return designation;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String employeeId;
        private String fullName;
        private String email;
        private String designation;
        private String status;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder employeeId(String employeeId) {
            this.employeeId = employeeId;
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

        public Builder designation(String designation) {
            this.designation = designation;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public TeamMemberResponse build() {
            return new TeamMemberResponse(this);
        }
    }
}
