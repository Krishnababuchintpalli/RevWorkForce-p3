package com.revworkforce.employee.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class EmployeeCreateRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phone;

    private String address;

    private String emergencyContact;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotNull(message = "Designation ID is required")
    private Long designationId;

    @NotNull(message = "Joining date is required")
    @PastOrPresent(message = "Joining date cannot be in the future")
    private LocalDate joiningDate;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be positive")
    private Double salary;

    private Long managerId;

    @NotNull(message = "Role ID is required")
    private Integer roleId;

    // Constructors
    public EmployeeCreateRequest() {
    }

    private EmployeeCreateRequest(Builder builder) {
        this.userId = builder.userId;
        this.email = builder.email;
        this.fullName = builder.fullName;
        this.phone = builder.phone;
        this.address = builder.address;
        this.emergencyContact = builder.emergencyContact;
        this.departmentId = builder.departmentId;
        this.designationId = builder.designationId;
        this.joiningDate = builder.joiningDate;
        this.salary = builder.salary;
        this.managerId = builder.managerId;
        this.roleId = builder.roleId;
    }

    // Getters
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public Double getSalary() {
        return salary;
    }

    public Long getManagerId() {
        return managerId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    // Setters
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

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long userId;
        private String email;
        private String fullName;
        private String phone;
        private String address;
        private String emergencyContact;
        private Long departmentId;
        private Long designationId;
        private LocalDate joiningDate;
        private Double salary;
        private Long managerId;
        private Integer roleId;

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

        public Builder departmentId(Long departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public Builder designationId(Long designationId) {
            this.designationId = designationId;
            return this;
        }

        public Builder joiningDate(LocalDate joiningDate) {
            this.joiningDate = joiningDate;
            return this;
        }

        public Builder salary(Double salary) {
            this.salary = salary;
            return this;
        }

        public Builder managerId(Long managerId) {
            this.managerId = managerId;
            return this;
        }

        public Builder roleId(Integer roleId) {
            this.roleId = roleId;
            return this;
        }

        public EmployeeCreateRequest build() {
            return new EmployeeCreateRequest(this);
        }
    }
}
