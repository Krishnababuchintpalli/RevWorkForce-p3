package com.revworkforce.auth.controller;

import com.revworkforce.auth.dto.*;
import com.revworkforce.auth.entity.User;
import com.revworkforce.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }



    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        TokenRefreshResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@RequestParam Long userId) {
        authService.logout(userId);
        return ResponseEntity.ok(new MessageResponse("Logout successful"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(new MessageResponse("Password reset successful"));
    }

    @GetMapping("/validate")
    public ResponseEntity<UserValidationResponse> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        UserValidationResponse response = authService.validateToken(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        User user = authService.getUserById(userId);
        UserResponse response = UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roleId(user.getRoleId())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
        return ResponseEntity.ok(response);
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid authorization header");
    }

    // Helper DTO classes
    public static class MessageResponse {
        private String message;

        public MessageResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class UserResponse {
        private Long userId;
        private String email;
        private String fullName;
        private Integer roleId;
        private String status;
        private java.time.LocalDateTime createdAt;

        public UserResponse() {
        }

        public UserResponse(Long userId, String email, String fullName, Integer roleId, String status, java.time.LocalDateTime createdAt) {
            this.userId = userId;
            this.email = email;
            this.fullName = fullName;
            this.roleId = roleId;
            this.status = status;
            this.createdAt = createdAt;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public Integer getRoleId() {
            return roleId;
        }

        public void setRoleId(Integer roleId) {
            this.roleId = roleId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public java.time.LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(java.time.LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Long userId;
            private String email;
            private String fullName;
            private Integer roleId;
            private String status;
            private java.time.LocalDateTime createdAt;

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

            public Builder roleId(Integer roleId) {
                this.roleId = roleId;
                return this;
            }

            public Builder status(String status) {
                this.status = status;
                return this;
            }

            public Builder createdAt(java.time.LocalDateTime createdAt) {
                this.createdAt = createdAt;
                return this;
            }

            public UserResponse build() {
                return new UserResponse(userId, email, fullName, roleId, status, createdAt);
            }
        }
    }
}
