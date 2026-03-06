package com.revworkforce.employee.dto;

import jakarta.validation.constraints.Pattern;

public class EmployeeUpdateRequest {

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phone;

    private String address;

    private String emergencyContact;

    // Constructors
    public EmployeeUpdateRequest() {
    }

    private EmployeeUpdateRequest(Builder builder) {
        this.phone = builder.phone;
        this.address = builder.address;
        this.emergencyContact = builder.emergencyContact;
    }

    // Getters
    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    // Setters
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String phone;
        private String address;
        private String emergencyContact;

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

        public EmployeeUpdateRequest build() {
            return new EmployeeUpdateRequest(this);
        }
    }
}
