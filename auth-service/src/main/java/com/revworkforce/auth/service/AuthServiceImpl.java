package com.revworkforce.auth.service;

import com.revworkforce.auth.dto.*;
import com.revworkforce.auth.entity.RefreshToken;
import com.revworkforce.auth.entity.User;
import com.revworkforce.auth.enums.Role;
import com.revworkforce.auth.exception.AuthException;
import com.revworkforce.auth.exception.ResourceNotFoundException;
import com.revworkforce.auth.repository.RoleMasterRepository;
import com.revworkforce.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleMasterRepository roleMasterRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleMasterRepository roleMasterRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.roleMasterRepository = roleMasterRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException("Invalid email or password");
        }

        if ("INACTIVE".equals(user.getStatus())) {
            throw new AuthException("Account is inactive. Please contact administrator.");
        }

        Role role = Role.fromId(user.getRoleId());
        String accessToken = jwtService.generateToken(user.getId(), user.getEmail(), role);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(role.name())
                .build();
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email already exists");
        }

        // Validate role exists
        roleMasterRepository.findById(request.getRoleId())
                .orElseThrow(() -> new AuthException("Invalid role ID"));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .roleId(request.getRoleId())
                .status("ACTIVE")
                .build();

        user = userRepository.save(user);

        Role role = Role.fromId(user.getRoleId());
        String accessToken = jwtService.generateToken(user.getId(), user.getEmail(), role);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(role.name())
                .build();
    }

    @Override
    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Invalidate all refresh tokens for this user
        refreshTokenService.deleteByUserId(user.getId());
    }

    @Override
    public UserValidationResponse validateToken(String token) {
        try {
            if (jwtService.validateToken(token)) {
                Long userId = jwtService.extractUserId(token);
                String email = jwtService.extractEmail(token);
                String role = jwtService.extractRole(token);

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                return UserValidationResponse.builder()
                        .userId(userId)
                        .email(email)
                        .fullName(user.getFullName())
                        .role(role)
                        .valid(true)
                        .build();
            }
        } catch (Exception e) {
            // Token is invalid
        }

        return UserValidationResponse.builder()
                .valid(false)
                .build();
    }

    @Override
    @Transactional
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new AuthException("Invalid refresh token"));

        refreshToken = refreshTokenService.verifyExpiration(refreshToken);

        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Role role = Role.fromId(user.getRoleId());
        String accessToken = jwtService.generateToken(user.getId(), user.getEmail(), role);

        return TokenRefreshResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .build();
    }

    @Override
    @Transactional
    public void logout(Long userId) {
        refreshTokenService.deleteByUserId(userId);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }
}
