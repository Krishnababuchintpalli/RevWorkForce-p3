package com.revworkforce.admin.dto;

import java.time.LocalDateTime;

public class AdminNotificationResponse {

    private Long id;
    private String message;
    private boolean readFlag;
    private LocalDateTime createdAt;

    public AdminNotificationResponse() {
    }

    public AdminNotificationResponse(Long id, String message, boolean readFlag, LocalDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.readFlag = readFlag;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isReadFlag() {
        return readFlag;
    }

    public void setReadFlag(boolean readFlag) {
        this.readFlag = readFlag;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String message;
        private boolean readFlag;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder readFlag(boolean readFlag) {
            this.readFlag = readFlag;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AdminNotificationResponse build() {
            return new AdminNotificationResponse(id, message, readFlag, createdAt);
        }
    }
}
