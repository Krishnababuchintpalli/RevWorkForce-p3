package com.revworkforce.employee.dto;

import java.time.LocalDate;

public class EmployeeProfileResponse {

    private Long id;
    private Long userId;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String emergencyContact;
    private String departmentName;
    private String designationName;
    private LocalDate joiningDate;
    private String status;
    private String managerName;

    // Constructors
    public EmployeeProfileResponse() {
    }

    private EmployeeProfileResponse(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.email = builder.email;
        this.fullName = builder.fullName;
        this.phone = builder.phone;
        this.address = builder.address;
        this.emergencyContact = builder.emergencyContact;
        this.departmentName = builder.departmentName;
        this.designationName = builder.designationName;
        this.joiningDate = builder.joiningDate;
        this.status = builder.status;
        this.managerName = builder.managerName;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDesignationName() {
        return designationName;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public String getStatus() {
        return status;
    }

    public String getManagerName() {
        return managerName;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long userId;
        private String email;
        private String fullName;
        private String phone;
        private String address;
        private String emergencyContact;
        private String departmentName;
        private String designationName;
        private LocalDate joiningDate;
        private String status;
        private String managerName;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder emergencyContact(String emergencyContact) {
            this.emergencyContact = emergencyContact;
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

        public Builder joiningDate(LocalDate joiningDate) {
            this.joiningDate = joiningDate;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder managerName(String managerName) {
            this.managerName = managerName;
            return this;
        }

        public EmployeeProfileResponse build() {
            return new EmployeeProfileResponse(this);
        }
    }
}
