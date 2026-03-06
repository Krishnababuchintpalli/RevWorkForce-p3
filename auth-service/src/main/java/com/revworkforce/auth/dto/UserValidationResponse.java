package com.revworkforce.auth.dto;

public class UserValidationResponse {

    private Long userId;
    private String email;
    private String fullName;
    private String role;
    private boolean valid;

    public UserValidationResponse() {
    }

    public UserValidationResponse(Long userId, String email, String fullName, String role, boolean valid) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.valid = valid;
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

    public String getRole() {
        return role;
    }

    public boolean isValid() {
        return valid;
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

    public void setRole(String role) {
        this.role = role;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long userId;
        private String email;
        private String fullName;
        private String role;
        private boolean valid;

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

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder valid(boolean valid) {
            this.valid = valid;
            return this;
        }

        public UserValidationResponse build() {
            return new UserValidationResponse(userId, email, fullName, role, valid);
        }
    }
}
