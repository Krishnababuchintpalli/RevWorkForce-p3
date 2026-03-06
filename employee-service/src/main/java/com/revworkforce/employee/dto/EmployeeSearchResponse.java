package com.revworkforce.employee.dto;

public class EmployeeSearchResponse {

    private Long id;
    private String fullName;
    private String email;
    private String departmentName;
    private String designationName;

    // Constructors
    public EmployeeSearchResponse() {
    }

    private EmployeeSearchResponse(Builder builder) {
        this.id = builder.id;
        this.fullName = builder.fullName;
        this.email = builder.email;
        this.departmentName = builder.departmentName;
        this.designationName = builder.designationName;
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

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDesignationName() {
        return designationName;
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

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String fullName;
        private String email;
        private String departmentName;
        private String designationName;

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

        public Builder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public Builder designationName(String designationName) {
            this.designationName = designationName;
            return this;
        }

        public EmployeeSearchResponse build() {
            return new EmployeeSearchResponse(this);
        }
    }
}
