package com.revworkforce.admin.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_notifications")
public class AdminNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private boolean readFlag;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public AdminNotification() {
    }

    public AdminNotification(Long id, Long userId, String message, boolean readFlag, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.readFlag = readFlag;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.readFlag == false) {
            this.readFlag = false;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        private Long userId;
        private String message;
        private boolean readFlag;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
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

        public AdminNotification build() {
            return new AdminNotification(id, userId, message, readFlag, createdAt);
        }
    }
}
