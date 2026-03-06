package com.revworkforce.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Role ID is required")
    private Integer roleId;

    public RegisterRequest() {
    }

    public RegisterRequest(String email, String password, String fullName, Integer roleId) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.roleId = roleId;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String email;
        private String password;
        private String fullName;
        private Integer roleId;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder roleId(Integer roleId) {
            this.roleId = roleId;
            return this;
        }

        public RegisterRequest build() {
            return new RegisterRequest(email, password, fullName, roleId);
        }
    }
}
