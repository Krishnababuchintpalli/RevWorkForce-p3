package com.revworkforce.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NotificationSendRequest {

    private Long recipientId;

    @NotBlank(message = "Message is required")
    @Size(max = 1000, message = "Message cannot exceed 1000 characters")
    private String message;

    public NotificationSendRequest() {
    }

    public NotificationSendRequest(Long recipientId, String message) {
        this.recipientId = recipientId;
        this.message = message;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
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
        private Long recipientId;
        private String message;

        public Builder recipientId(Long recipientId) {
            this.recipientId = recipientId;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public NotificationSendRequest build() {
            return new NotificationSendRequest(recipientId, message);
        }
    }
}
