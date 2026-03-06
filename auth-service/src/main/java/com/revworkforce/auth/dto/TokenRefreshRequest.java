package com.revworkforce.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class TokenRefreshRequest {

    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

    public TokenRefreshRequest() {
    }

    public TokenRefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Getters
    public String getRefreshToken() {
        return refreshToken;
    }

    // Setters
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String refreshToken;

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public TokenRefreshRequest build() {
            return new TokenRefreshRequest(refreshToken);
        }
    }
}
