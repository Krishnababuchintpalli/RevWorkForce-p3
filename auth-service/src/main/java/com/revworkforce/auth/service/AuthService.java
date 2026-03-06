package com.revworkforce.auth.service;

import com.revworkforce.auth.dto.*;
import com.revworkforce.auth.entity.User;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);

    void resetPassword(PasswordResetRequest request);

    UserValidationResponse validateToken(String token);

    TokenRefreshResponse refreshToken(TokenRefreshRequest request);

    void logout(Long userId);

    User getUserById(Long userId);
}
