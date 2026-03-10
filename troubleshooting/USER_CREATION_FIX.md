# User Creation Feature Fix - Microservices Migration

## Problem Statement

The user creation feature from the frontend does not work in the microservices setup (RevWorkForce-p3), while it works correctly in the monolithic project (RevWorkForce-p2).

## Root Cause Analysis

### Issue 1: Missing `/api/users` Endpoint in Auth-Service

The monolithic backend has a `UserController` that handles user management at `/api/users`:
- `GET /api/users` - List all users (admin only)
- `POST /api/users` - Create user (admin only)
- `PATCH /api/users/{id}/active` - Activate/deactivate user
- `GET /api/users/search` - Search users
- `GET /api/users/me` - Get current user profile

The microservices auth-service only has `/api/auth/*` endpoints for authentication (login, register, logout, etc.) but **no user management endpoints**.

### Issue 2: Incorrect API Gateway Routing

The API Gateway was configured to route `/api/users/**` to the employee-service with a path rewrite to `/api/employees/**`. This is incorrect because:
1. The employee-service expects different request/response formats
2. User authentication data should be managed by auth-service

### Issue 3: Missing `employeeId` Field

The auth-service `User` entity was missing the `employeeId` field that the frontend sends when creating users.

---

## Solution Overview

1. Add `employeeId` field to the User entity in auth-service
2. Create `AdminCreateUserRequest` DTO matching frontend request format
3. Create `UserListResponse` DTO for API responses
4. Add user management methods to AuthService
5. Create `UserController` with `/api/users` endpoints
6. Update API Gateway routing to send `/api/users/**` to auth-service

---

## Detailed Changes

### 1. Update User Entity

**File:** `RevWorkForce-p3/auth-service/src/main/java/com/revworkforce/auth/entity/User.java`

Add the `employeeId` field:

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String employeeId;  // ADD THIS FIELD

    @Column(unique = true, nullable = false)
    private String email;

    // ... rest of existing fields
}
```

Add getter and setter:

```java
public String getEmployeeId() {
    return employeeId;
}

public void setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
}
```

Update the constructor to include `employeeId`:

```java
public User(Long id, String employeeId, String email, String password,
            String fullName, Integer roleId, String status, LocalDateTime createdAt) {
    this.id = id;
    this.employeeId = employeeId;
    this.email = email;
    this.password = password;
    this.fullName = fullName;
    this.roleId = roleId;
    this.status = status;
    this.createdAt = createdAt;
}
```

Update the Builder class to include `employeeId`:

```java
public static class Builder {
    private Long id;
    private String employeeId;  // ADD THIS
    private String email;
    // ... other fields

    public Builder employeeId(String employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public User build() {
        return new User(id, employeeId, email, password, fullName, roleId, status, createdAt);
    }
}
```

---

### 2. Update UserRepository

**File:** `RevWorkForce-p3/auth-service/src/main/java/com/revworkforce/auth/repository/UserRepository.java`

Add new repository methods:

```java
package com.revworkforce.auth.repository;

import com.revworkforce.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // ADD THESE NEW METHODS
    Optional<User> findByEmployeeId(String employeeId);

    boolean existsByEmployeeId(String employeeId);

    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR u.employeeId LIKE CONCAT('%', :query, '%')")
    List<User> searchByNameOrEmployeeId(@Param("query") String query);
}
```

---

### 3. Create AdminCreateUserRequest DTO

**File:** `RevWorkForce-p3/auth-service/src/main/java/com/revworkforce/auth/dto/AdminCreateUserRequest.java` (NEW FILE)

```java
package com.revworkforce.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AdminCreateUserRequest {

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid format")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=\\S+$)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
            message = "Password must be at least 8 characters and include uppercase, lowercase, number, and special character"
    )
    private String password;

    private String role = "EMPLOYEE";

    private Long managerId;

    public AdminCreateUserRequest() {
    }

    public AdminCreateUserRequest(String employeeId, String fullName, String email,
                                   String password, String role, Long managerId) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.managerId = managerId;
    }

    // Getters
    public String getEmployeeId() { return employeeId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public Long getManagerId() { return managerId; }

    // Setters
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setManagerId(Long managerId) { this.managerId = managerId; }
}
```

---

### 4. Create UserListResponse DTO

**File:** `RevWorkForce-p3/auth-service/src/main/java/com/revworkforce/auth/dto/UserListResponse.java` (NEW FILE)

```java
package com.revworkforce.auth.dto;

import com.revworkforce.auth.entity.User;
import com.revworkforce.auth.enums.Role;

public class UserListResponse {

    private Long id;
    private String employeeId;
    private String fullName;
    private String email;
    private String role;
    private boolean active;

    public UserListResponse() {
    }

    public UserListResponse(User user) {
        this.id = user.getId();
        this.employeeId = user.getEmployeeId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.role = Role.fromId(user.getRoleId()).name();
        this.active = "ACTIVE".equals(user.getStatus());
    }

    // Getters
    public Long getId() { return id; }
    public String getEmployeeId() { return employeeId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public boolean isActive() { return active; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setActive(boolean active) { this.active = active; }
}
```

---

### 5. Create UnauthorizedException

**File:** `RevWorkForce-p3/auth-service/src/main/java/com/revworkforce/auth/exception/UnauthorizedException.java` (NEW FILE)

```java
package com.revworkforce.auth.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

---

### 6. Update GlobalExceptionHandler

**File:** `RevWorkForce-p3/auth-service/src/main/java/com/revworkforce/auth/exception/GlobalExceptionHandler.java`

Add handlers for new exceptions (add after `handleResourceNotFoundException`):

```java
@ExceptionHandler(UnauthorizedException.class)
public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.FORBIDDEN.value(),
            "Forbidden",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
}

@ExceptionHandler(IllegalArgumentException.class)
public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
    Map<String, String> response = new HashMap<>();
    response.put("error", ex.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}
```

---

### 7. Update AuthService Interface

**File:** `RevWorkForce-p3/auth-service/src/main/java/com/revworkforce/auth/service/AuthService.java`

Add new method signatures:

```java
package com.revworkforce.auth.service;

import com.revworkforce.auth.dto.*;
import com.revworkforce.auth.entity.User;

import java.util.List;

public interface AuthService {

    // Existing methods...
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
    void resetPassword(PasswordResetRequest request);
    UserValidationResponse validateToken(String token);
    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
    void logout(Long userId);
    User getUserById(Long userId);

    // ADD THESE NEW METHODS
    User createUser(AdminCreateUserRequest request);
    List<User> getAllUsers();
    List<User> searchUsers(String query);
    User setUserActive(Long userId, boolean active);
}
```

---

### 8. Update AuthServiceImpl

**File:** `RevWorkForce-p3/auth-service/src/main/java/com/revworkforce/auth/service/AuthServiceImpl.java`

Add import at the top:
```java
import java.util.List;
```

Add new method implementations at the end of the class:

```java
@Override
@Transactional
public User createUser(AdminCreateUserRequest request) {
    // Validate employee ID is numeric
    try {
        Long.parseLong(request.getEmployeeId().trim());
    } catch (NumberFormatException ex) {
        throw new IllegalArgumentException("Employee ID must be numeric");
    }

    // Check for duplicate employee ID
    if (userRepository.existsByEmployeeId(request.getEmployeeId().trim())) {
        throw new IllegalArgumentException("Employee ID already exists");
    }

    // Check for duplicate email
    if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
        throw new IllegalArgumentException("Email already exists");
    }

    // Convert role string to role ID
    Role role;
    try {
        role = Role.valueOf(request.getRole().toUpperCase());
    } catch (IllegalArgumentException e) {
        role = Role.EMPLOYEE;
    }

    User user = User.builder()
            .employeeId(request.getEmployeeId().trim())
            .fullName(request.getFullName().trim())
            .email(request.getEmail().trim().toLowerCase())
            .password(passwordEncoder.encode(request.getPassword()))
            .roleId(role.getId())
            .status("ACTIVE")
            .build();

    return userRepository.save(user);
}

@Override
public List<User> getAllUsers() {
    return userRepository.findAll();
}

@Override
public List<User> searchUsers(String query) {
    return userRepository.searchByNameOrEmployeeId(query);
}

@Override
@Transactional
public User setUserActive(Long userId, boolean active) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    user.setStatus(active ? "ACTIVE" : "INACTIVE");
    return userRepository.save(user);
}
```

---

### 9. Create UserController

**File:** `RevWorkForce-p3/auth-service/src/main/java/com/revworkforce/auth/controller/UserController.java` (NEW FILE)

```java
package com.revworkforce.auth.controller;

import com.revworkforce.auth.dto.AdminCreateUserRequest;
import com.revworkforce.auth.dto.UserListResponse;
import com.revworkforce.auth.entity.User;
import com.revworkforce.auth.exception.UnauthorizedException;
import com.revworkforce.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<UserListResponse>> getAllUsers(
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        if (!"ADMIN".equals(role)) {
            throw new UnauthorizedException("Only admins can view all users");
        }
        List<User> users = authService.getAllUsers();
        List<UserListResponse> response = users.stream()
                .map(UserListResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserListResponse> createUser(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @Valid @RequestBody AdminCreateUserRequest request) {
        if (!"ADMIN".equals(role)) {
            throw new UnauthorizedException("Only admins can create users");
        }
        User user = authService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserListResponse(user));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserListResponse>> searchUsers(@RequestParam String q) {
        List<User> users = authService.searchUsers(q);
        List<UserListResponse> response = users.stream()
                .map(UserListResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<UserListResponse> setUserActive(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @PathVariable Long id,
            @RequestParam boolean active) {
        if (!"ADMIN".equals(role)) {
            throw new UnauthorizedException("Only admins can activate/deactivate users");
        }
        User user = authService.setUserActive(id, active);
        return ResponseEntity.ok(new UserListResponse(user));
    }

    @GetMapping("/me")
    public ResponseEntity<UserListResponse> getCurrentUser(
            @RequestHeader("X-User-Id") Long userId) {
        User user = authService.getUserById(userId);
        return ResponseEntity.ok(new UserListResponse(user));
    }
}
```

---

### 10. Update API Gateway RouteConfig

**File:** `RevWorkForce-p3/api-gateway/src/main/java/com/revworkforce/gateway/config/RouteConfig.java`

Change the users route from:

```java
// OLD - INCORRECT
.route("users-alias", r -> r.path("/api/users", "/api/users/**")
        .filters(f -> f
                .filter(jwtFilter)
                .rewritePath("/api/users(?<segment>/?.*)", "/api/employees${segment}"))
        .uri("lb://employee-service"))
```

To:

```java
// NEW - CORRECT
.route("users-service", r -> r.path("/api/users", "/api/users/**")
        .filters(f -> f.filter(jwtFilter))
        .uri("lb://auth-service"))
```

---

## Database Changes

No manual database changes required. The `hibernate.ddl-auto: update` configuration in `application.yml` will automatically add the `employee_id` column to the `users` table when the auth-service starts.

---

## Files Summary

### Modified Files
| File | Change |
|------|--------|
| `auth-service/.../entity/User.java` | Added `employeeId` field |
| `auth-service/.../repository/UserRepository.java` | Added new query methods |
| `auth-service/.../service/AuthService.java` | Added new method signatures |
| `auth-service/.../service/AuthServiceImpl.java` | Implemented new methods |
| `auth-service/.../exception/GlobalExceptionHandler.java` | Added exception handlers |
| `api-gateway/.../config/RouteConfig.java` | Fixed routing for `/api/users` |

### New Files
| File | Purpose |
|------|---------|
| `auth-service/.../dto/AdminCreateUserRequest.java` | Request DTO for user creation |
| `auth-service/.../dto/UserListResponse.java` | Response DTO for user data |
| `auth-service/.../controller/UserController.java` | REST controller for user management |
| `auth-service/.../exception/UnauthorizedException.java` | Custom exception for authorization |

---

## Testing

After applying these changes:

1. Start the services in order:
   - Discovery Server (port 8761)
   - Config Server
   - Auth Service (port 8081)
   - API Gateway (port 8080)

2. Login as admin through the frontend

3. Test user creation:
   - Navigate to Admin panel
   - Fill in the "Add Employee" form
   - Submit and verify the user is created

4. Verify the API directly:
   ```bash
   # Get auth token first
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "admin@example.com", "password": "password"}'

   # Create user (use the token from above)
   curl -X POST http://localhost:8080/api/users \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer <token>" \
     -d '{
       "employeeId": "12345",
       "fullName": "John Doe",
       "email": "john.doe@example.com",
       "password": "Password123!",
       "role": "EMPLOYEE"
     }'
   ```

---

## Comparison: Monolithic vs Microservices

| Aspect | Monolithic (P2) | Microservices (P3) - After Fix |
|--------|-----------------|-------------------------------|
| User Creation Endpoint | `POST /api/users` | `POST /api/users` (via API Gateway) |
| User List Endpoint | `GET /api/users` | `GET /api/users` (via API Gateway) |
| Activate/Deactivate | `PATCH /api/users/{id}/active` | `PATCH /api/users/{id}/active` |
| Authorization | `@PreAuthorize("hasRole('ADMIN')")` | `X-User-Role` header check |
| Service | Single backend | Auth-service |
| Request Format | Same | Same |
| Response Format | Same | Same |
