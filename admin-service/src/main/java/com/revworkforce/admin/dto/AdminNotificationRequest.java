package com.revworkforce.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AdminNotificationRequest {

    private Long userId;

    @NotBlank(message = "Message is required")
    @Size(max = 1000, message = "Message cannot exceed 1000 characters")
    private String message;

    public AdminNotificationRequest() {
    }

    public AdminNotificationRequest(Long userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long userId;
        private String message;

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public AdminNotificationRequest build() {
            return new AdminNotificationRequest(userId, message);
        }
    }
}
