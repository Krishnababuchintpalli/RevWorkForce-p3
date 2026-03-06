package com.revworkforce.leave.dto;

import java.time.LocalDateTime;

public class LeaveNotificationResponse {

    private Long id;
    private String message;
    private boolean readFlag;
    private LocalDateTime createdAt;

    // Constructors
    public LeaveNotificationResponse() {
    }

    public LeaveNotificationResponse(Long id, String message, boolean readFlag, LocalDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.readFlag = readFlag;
        this.createdAt = createdAt;
    }

    // Getters and Setters
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
}
