# RevWorkForce P3 Microservices - Developer Commit Instructions

This document provides step-by-step commit instructions for the RevWorkForce P3 microservices project using a **single shared mono-repo strategy**.

---

## Table of Contents
1. [Project Overview](#project-overview)
2. [Commit Strategy](#commit-strategy)
3. [Team Member Assignments](#team-member-assignments)
4. [Commit Order](#commit-order)
5. [Developer Instructions](#developer-instructions)
   - [Krishna Babu - Infrastructure Setup](#krishna-babu---infrastructure-setup-first)
   - [Ganesh - auth-service](#ganesh---auth-service)
   - [Vamsi - employee-service](#vamsi---employee-service)
   - [Krishna Babu - admin-service](#krishna-babu---admin-service)
   - [Sai Krishna - leave-service](#sai-krishna---leave-service)
   - [Saideep Reddy - performance-service](#saideep-reddy---performance-service)

---

## Project Overview

**Repository**: Single mono-repo containing all microservices
**Location**: `/Users/vimalkrishnan/Workspace/revature/2353/review/p2/repos/p3-revworkforce`
**Strategy**: Feature-based commits (5-10 commits per developer)

### Microservices Architecture

```
p3-revworkforce/
├── pom.xml                    # Parent POM
├── .gitignore                 # Git ignore rules
├── docker-compose.yml         # Docker orchestration
├── README.md                  # Project documentation
├── config-server/             # Centralized configuration (Port 8888)
├── discovery-server/          # Eureka service registry (Port 8761)
├── api-gateway/               # API Gateway (Port 8080)
├── auth-service/              # Authentication & Authorization (Port 8081)
├── employee-service/          # Employee & Directory Management (Port 8082)
├── leave-service/             # Leave Management (Port 8083)
├── performance-service/       # Performance, Goals & Feedback (Port 8084)
└── admin-service/             # Admin, Master Data & Notifications (Port 8085)
```

---

## Commit Strategy

### Feature-Based Commits (5-10 per developer)
Each developer should create commits that represent complete, logical features:

1. **Setup & Configuration** - pom.xml, application.yml, main Application class
2. **Entities/Models** - Database entities with JPA annotations
3. **DTOs** - Request/Response data transfer objects
4. **Repositories** - JPA repositories
5. **Services** - Business logic implementation
6. **Controllers** - REST endpoints
7. **Configuration Classes** - Security, Feign, CORS, etc.
8. **Exception Handling** - Custom exceptions and global handlers
9. **Additional Features** - Enums, utilities, data loaders, etc.
10. **Testing & Documentation** - (Optional) Tests and additional docs

---

## Team Member Assignments

| Developer | Service(s) | Port(s) | Responsibilities |
|-----------|-----------|---------|------------------|
| **Krishna Babu** | Infrastructure + admin-service | 8085 | API Gateway, Config Server, Discovery Server, Docker Compose, Parent POM, .gitignore, README, Admin Service |
| **Ganesh** | auth-service | 8081 | Authentication, Authorization, JWT, User Management |
| **Vamsi** | employee-service | 8082 | Employee Management, Directory, Profile |
| **Sai Krishna** | leave-service | 8083 | Leave Management, Leave Notifications |
| **Saideep Reddy** | performance-service | 8084 | Performance Reviews, Goals, Feedback |

---

## Commit Order

**CRITICAL**: Follow this exact order to avoid dependency issues.

1. **Krishna Babu** - Infrastructure (FIRST - creates the mono-repo foundation)
2. **Ganesh** - auth-service (Independent, needed by all other services)
3. **Vamsi** - employee-service (Depends on auth-service)
4. **Krishna Babu** - admin-service (Can run in parallel with employee-service)
5. **Sai Krishna** - leave-service (Depends on auth-service and employee-service)
6. **Saideep Reddy** - performance-service (Depends on auth-service and employee-service)

---

## Developer Instructions

---

## Krishna Babu - Infrastructure Setup (FIRST)

**Responsibility**: Set up the entire mono-repo infrastructure including parent POM, infrastructure services, and docker-compose configuration.

### Pre-requisites

```bash
# Configure Git with your identity
git config --global user.name "Krishna Babu"
git config --global user.email "krishna.babu@revature.com"

# Navigate to the project root
cd /Users/vimalkrishnan/Workspace/revature/2353/review/p2/repos/p3-revworkforce
```

### Commit 1: Initialize Repository with Parent POM and .gitignore

**What**: Create the foundational mono-repo structure with parent POM and Git ignore rules.

```bash
# Add parent POM and .gitignore
git add pom.xml .gitignore

# Commit
git commit -m "$(cat <<'EOF'
feat: Initialize P3 RevWorkForce mono-repo with parent POM

- Add parent pom.xml with Spring Boot 3.2 and Spring Cloud dependencies
- Configure dependency management for all microservices
- Add .gitignore for Java, Maven, IDE files
- Set up multi-module structure for microservices architecture

Files:
- pom.xml: Parent POM with Spring Boot 3.2.x and Spring Cloud 2023.0.x
- .gitignore: Comprehensive ignore rules for Java/Maven projects

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 2: Add Config Server

**What**: Centralized configuration management service.

```bash
# Add all config-server files
git add config-server/

# Commit
git commit -m "$(cat <<'EOF'
feat: Add Config Server for centralized configuration

- Create config-server module with Spring Cloud Config Server
- Configure server port 8888
- Add pom.xml with spring-cloud-config-server dependency
- Create ConfigServerApplication.java with @EnableConfigServer

Files:
- config-server/pom.xml
- config-server/src/main/resources/application.yml
- config-server/src/main/java/com/revworkforce/config/ConfigServerApplication.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 3: Add Discovery Server (Eureka)

**What**: Service registry for service discovery.

```bash
# Add all discovery-server files
git add discovery-server/

# Commit
git commit -m "$(cat <<'EOF'
feat: Add Eureka Discovery Server for service registry

- Create discovery-server module with Spring Cloud Netflix Eureka
- Configure server port 8761
- Add pom.xml with spring-cloud-starter-netflix-eureka-server
- Create DiscoveryServerApplication.java with @EnableEurekaServer
- Configure standalone mode (registerWithEureka: false, fetchRegistry: false)

Files:
- discovery-server/pom.xml
- discovery-server/src/main/resources/application.yml
- discovery-server/src/main/java/com/revworkforce/discovery/DiscoveryServerApplication.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 4: Add API Gateway

**What**: Single entry point for all microservices with routing and load balancing.

```bash
# Add all api-gateway files
git add api-gateway/

# Commit
git commit -m "$(cat <<'EOF'
feat: Add API Gateway with Spring Cloud Gateway

- Create api-gateway module with Spring Cloud Gateway
- Configure server port 8080
- Add pom.xml with spring-cloud-starter-gateway dependency
- Create ApiGatewayApplication.java
- Configure routes for all microservices (auth, employee, leave, performance, admin)
- Enable Eureka client for service discovery
- Configure CORS for frontend integration

Files:
- api-gateway/pom.xml
- api-gateway/src/main/resources/application.yml
- api-gateway/src/main/java/com/revworkforce/gateway/ApiGatewayApplication.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 5: Add Docker Compose Configuration

**What**: Docker orchestration for running all services.

```bash
# Add docker-compose.yml
git add docker-compose.yml

# Commit
git commit -m "$(cat <<'EOF'
feat: Add Docker Compose for microservices orchestration

- Create docker-compose.yml with all 8 services
- Configure service dependencies (auth before others)
- Add health checks for all services
- Configure networking for inter-service communication
- Set up environment variables and ports

Services:
- config-server (8888)
- discovery-server (8761)
- api-gateway (8080)
- auth-service (8081)
- employee-service (8082)
- leave-service (8083)
- performance-service (8084)
- admin-service (8085)

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 6: Add Project README

**What**: Comprehensive project documentation.

```bash
# Add README.md
git add README.md

# Commit
git commit -m "$(cat <<'EOF'
docs: Add comprehensive project README

- Add project overview and architecture diagram
- Document all 8 microservices with ports and responsibilities
- Add setup instructions for running locally
- Include Docker Compose commands
- Document API endpoints for each service
- Add technology stack and dependencies
- Include team member assignments

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

**CHECKPOINT**: At this point, Krishna Babu has completed the infrastructure setup. Other developers can now start their services.

---

## Ganesh - auth-service

**Responsibility**: Authentication & Authorization service with JWT token management.

### Pre-requisites

```bash
# Configure Git with your identity
git config --global user.name "Ganesh"
git config --global user.email "ganesh@revature.com"

# Navigate to the project root
cd /Users/vimalkrishnan/Workspace/revature/2353/review/p2/repos/p3-revworkforce

# Ensure you have the latest infrastructure changes
git pull origin main
```

### Commit 1: Setup auth-service project structure

**What**: Create the basic project structure with pom.xml, application.yml, and main application class.

```bash
# Add setup files
git add auth-service/pom.xml \
        auth-service/src/main/resources/application.yml \
        auth-service/src/main/java/com/revworkforce/auth/AuthServiceApplication.java

# Commit
git commit -m "$(cat <<'EOF'
feat(auth): Setup auth-service project structure

- Add pom.xml with Spring Boot, Security, JPA, H2 dependencies
- Configure application.yml with port 8081, H2 database, Eureka client
- Create AuthServiceApplication.java main class
- Enable Eureka client registration

Service: auth-service
Port: 8081

Files:
- auth-service/pom.xml
- auth-service/src/main/resources/application.yml
- auth-service/src/main/java/com/revworkforce/auth/AuthServiceApplication.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 2: Add entities and enums

**What**: Create User, RefreshToken, RoleMaster entities and Role enum.

```bash
# Add all entity files
git add auth-service/src/main/java/com/revworkforce/auth/entity/ \
        auth-service/src/main/java/com/revworkforce/auth/enums/

# Commit
git commit -m "$(cat <<'EOF'
feat(auth): Add User, RefreshToken, RoleMaster entities and Role enum

- Create User entity with userId, email, password, role fields
- Add RefreshToken entity for token refresh mechanism
- Create RoleMaster entity for role management
- Add Role enum with EMPLOYEE, MANAGER, ADMIN values
- Configure JPA annotations and relationships

Entities:
- User: Core user authentication data
- RefreshToken: JWT refresh token storage
- RoleMaster: Role master data
- Role enum: User role types

Files:
- auth-service/src/main/java/com/revworkforce/auth/entity/User.java
- auth-service/src/main/java/com/revworkforce/auth/entity/RefreshToken.java
- auth-service/src/main/java/com/revworkforce/auth/entity/RoleMaster.java
- auth-service/src/main/java/com/revworkforce/auth/enums/Role.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 3: Add DTOs for requests and responses

**What**: Create all DTOs for authentication operations.

```bash
# Add all DTO files
git add auth-service/src/main/java/com/revworkforce/auth/dto/

# Commit
git commit -m "$(cat <<'EOF'
feat(auth): Add DTOs for authentication operations

- Create LoginRequest for user login credentials
- Add AuthResponse for JWT token response
- Create RegisterRequest for new user registration
- Add PasswordResetRequest for password reset
- Create TokenRefreshRequest and TokenRefreshResponse
- Add UserValidationResponse for internal service validation
- Include validation annotations (@NotBlank, @Email, etc.)

DTOs:
- LoginRequest: email, password
- AuthResponse: accessToken, refreshToken, userId, email, role
- RegisterRequest: email, password, firstName, lastName, role
- PasswordResetRequest: email, oldPassword, newPassword
- TokenRefreshRequest: refreshToken
- TokenRefreshResponse: accessToken, refreshToken
- UserValidationResponse: userId, email, role, valid

Files:
- auth-service/src/main/java/com/revworkforce/auth/dto/LoginRequest.java
- auth-service/src/main/java/com/revworkforce/auth/dto/AuthResponse.java
- auth-service/src/main/java/com/revworkforce/auth/dto/RegisterRequest.java
- auth-service/src/main/java/com/revworkforce/auth/dto/PasswordResetRequest.java
- auth-service/src/main/java/com/revworkforce/auth/dto/TokenRefreshRequest.java
- auth-service/src/main/java/com/revworkforce/auth/dto/TokenRefreshResponse.java
- auth-service/src/main/java/com/revworkforce/auth/dto/UserValidationResponse.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 4: Add repositories

**What**: Create JPA repositories for database access.

```bash
# Add all repository files
git add auth-service/src/main/java/com/revworkforce/auth/repository/

# Commit
git commit -m "$(cat <<'EOF'
feat(auth): Add JPA repositories for User, RefreshToken, RoleMaster

- Create UserRepository with findByEmail method
- Add RefreshTokenRepository with findByToken and deleteByUser
- Create RoleMasterRepository with findByRoleName
- Extend JpaRepository for CRUD operations

Repositories:
- UserRepository: User data access
- RefreshTokenRepository: Refresh token management
- RoleMasterRepository: Role master data access

Files:
- auth-service/src/main/java/com/revworkforce/auth/repository/UserRepository.java
- auth-service/src/main/java/com/revworkforce/auth/repository/RefreshTokenRepository.java
- auth-service/src/main/java/com/revworkforce/auth/repository/RoleMasterRepository.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 5: Add service layer

**What**: Implement business logic for authentication, JWT generation, and token refresh.

```bash
# Add all service files
git add auth-service/src/main/java/com/revworkforce/auth/service/

# Commit
git commit -m "$(cat <<'EOF'
feat(auth): Add AuthService, JwtService, and RefreshTokenService

- Create AuthService for login, register, logout, password reset
- Implement JwtService for JWT token generation and validation
- Add RefreshTokenService for refresh token management
- Implement BCrypt password hashing
- Add token expiration handling (24 hours for JWT, 7 days for refresh)
- Implement user validation endpoint for other services

Services:
- AuthService: Core authentication logic
- JwtService: JWT token generation, validation, claims extraction
- RefreshTokenService: Refresh token CRUD and expiration management

Key Features:
- HMAC SHA256 token signing
- Role-based token claims
- Secure password hashing with BCrypt
- Token refresh mechanism

Files:
- auth-service/src/main/java/com/revworkforce/auth/service/AuthService.java
- auth-service/src/main/java/com/revworkforce/auth/service/JwtService.java
- auth-service/src/main/java/com/revworkforce/auth/service/RefreshTokenService.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 6: Add REST controller

**What**: Create REST endpoints for authentication operations.

```bash
# Add controller file
git add auth-service/src/main/java/com/revworkforce/auth/controller/

# Commit
git commit -m "$(cat <<'EOF'
feat(auth): Add AuthController with REST endpoints

- POST /api/auth/login - Authenticate user and return JWT
- POST /api/auth/register - Register new user (admin only)
- POST /api/auth/refresh - Refresh JWT token
- POST /api/auth/logout - Logout and invalidate refresh token
- POST /api/auth/reset-password - Reset user password
- GET /api/auth/validate - Validate JWT token (internal endpoint)
- GET /api/auth/user/{userId} - Get user by ID (internal endpoint)

Controller:
- AuthController: All authentication endpoints
- Request/Response DTOs for all operations
- Proper HTTP status codes and error handling

Files:
- auth-service/src/main/java/com/revworkforce/auth/controller/AuthController.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 7: Add security configuration

**What**: Configure Spring Security, JWT filter, and CORS.

```bash
# Add all config files
git add auth-service/src/main/java/com/revworkforce/auth/config/

# Commit
git commit -m "$(cat <<'EOF'
feat(auth): Add SecurityConfig, DataLoader, and WebConfig

- Create SecurityConfig with Spring Security filter chain
- Configure public endpoints (/api/auth/login, /api/auth/register)
- Add CORS configuration for frontend integration
- Implement BCryptPasswordEncoder bean
- Create DataLoader for initial user and role data seeding
- Add WebConfig for additional web configurations

Configuration:
- SecurityConfig: Spring Security with stateless sessions
- DataLoader: Seeds admin user and role master data
- WebConfig: CORS and web MVC configuration

Default Admin User:
- Email: admin@revature.com
- Password: admin123
- Role: ADMIN

Files:
- auth-service/src/main/java/com/revworkforce/auth/config/SecurityConfig.java
- auth-service/src/main/java/com/revworkforce/auth/config/DataLoader.java
- auth-service/src/main/java/com/revworkforce/auth/config/WebConfig.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 8: Add exception handling

**What**: Create custom exceptions and global exception handler.

```bash
# Add exception files
git add auth-service/src/main/java/com/revworkforce/auth/exception/

# Commit
git commit -m "$(cat <<'EOF'
feat(auth): Add exception handling with custom exceptions

- Create InvalidCredentialsException for login failures
- Add TokenExpiredException for expired JWT tokens
- Create UserAlreadyExistsException for duplicate registration
- Add GlobalExceptionHandler with @RestControllerAdvice
- Implement proper HTTP status codes and error messages
- Add ErrorResponse DTO for consistent error format

Exceptions:
- InvalidCredentialsException: HTTP 401
- TokenExpiredException: HTTP 401
- UserAlreadyExistsException: HTTP 409
- ResourceNotFoundException: HTTP 404
- UnauthorizedException: HTTP 403

Files:
- auth-service/src/main/java/com/revworkforce/auth/exception/InvalidCredentialsException.java
- auth-service/src/main/java/com/revworkforce/auth/exception/TokenExpiredException.java
- auth-service/src/main/java/com/revworkforce/auth/exception/UserAlreadyExistsException.java
- auth-service/src/main/java/com/revworkforce/auth/exception/ResourceNotFoundException.java
- auth-service/src/main/java/com/revworkforce/auth/exception/UnauthorizedException.java
- auth-service/src/main/java/com/revworkforce/auth/exception/GlobalExceptionHandler.java
- auth-service/src/main/java/com/revworkforce/auth/exception/ErrorResponse.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

**CHECKPOINT**: auth-service is complete. Other services can now integrate with it.

---

## Vamsi - employee-service

**Responsibility**: Employee and Directory management service.

### Pre-requisites

```bash
# Configure Git with your identity
git config --global user.name "Vamsi"
git config --global user.email "vamsi@revature.com"

# Navigate to the project root
cd /Users/vimalkrishnan/Workspace/revature/2353/review/p2/repos/p3-revworkforce

# Ensure you have the latest changes (especially auth-service)
git pull origin main
```

### Commit 1: Setup employee-service project structure

**What**: Create the basic project structure with pom.xml, application.yml, and main application class.

```bash
# Add setup files
git add employee-service/pom.xml \
        employee-service/src/main/resources/application.yml \
        employee-service/src/main/java/com/revworkforce/employee/EmployeeServiceApplication.java

# Commit
git commit -m "$(cat <<'EOF'
feat(employee): Setup employee-service project structure

- Add pom.xml with Spring Boot, JPA, H2, Feign dependencies
- Configure application.yml with port 8082, H2 database, Eureka client
- Create EmployeeServiceApplication.java main class
- Enable Eureka client and Feign clients

Service: employee-service
Port: 8082

Files:
- employee-service/pom.xml
- employee-service/src/main/resources/application.yml
- employee-service/src/main/java/com/revworkforce/employee/EmployeeServiceApplication.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 2: Add Employee entity

**What**: Create Employee entity with all fields.

```bash
# Add entity file
git add employee-service/src/main/java/com/revworkforce/employee/entity/

# Commit
git commit -m "$(cat <<'EOF'
feat(employee): Add Employee entity

- Create Employee entity with employeeId as primary key
- Add fields: userId, firstName, lastName, email, phoneNumber, department, designation
- Add manager relationship (self-referencing ManyToOne)
- Include joinDate, isActive, dateOfBirth, address fields
- Configure JPA annotations and relationships

Entity:
- Employee: Core employee profile data linked to auth-service userId

Files:
- employee-service/src/main/java/com/revworkforce/employee/entity/Employee.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 3: Add DTOs for employee operations

**What**: Create all DTOs for employee CRUD and directory operations.

```bash
# Add all DTO files
git add employee-service/src/main/java/com/revworkforce/employee/dto/

# Commit
git commit -m "$(cat <<'EOF'
feat(employee): Add DTOs for employee operations

- Create EmployeeCreateRequest for creating new employees
- Add EmployeeUpdateRequest for profile updates
- Create EmployeeResponse for employee details
- Add EmployeeProfileResponse for profile view
- Create EmployeeSearchResponse for search results
- Add TeamMemberResponse for team view
- Create EmployeeSummaryResponse for internal service calls
- Add ManagerAssignRequest for manager assignment
- Include validation annotations

DTOs:
- EmployeeCreateRequest: userId, firstName, lastName, email, department, designation
- EmployeeUpdateRequest: firstName, lastName, phoneNumber, address
- EmployeeResponse: Complete employee details
- EmployeeProfileResponse: Profile view with manager info
- EmployeeSearchResponse: Search results with basic info
- TeamMemberResponse: Team member info for managers
- EmployeeSummaryResponse: Summary for other services

Files:
- employee-service/src/main/java/com/revworkforce/employee/dto/EmployeeCreateRequest.java
- employee-service/src/main/java/com/revworkforce/employee/dto/EmployeeUpdateRequest.java
- employee-service/src/main/java/com/revworkforce/employee/dto/EmployeeResponse.java
- employee-service/src/main/java/com/revworkforce/employee/dto/EmployeeProfileResponse.java
- employee-service/src/main/java/com/revworkforce/employee/dto/EmployeeSearchResponse.java
- employee-service/src/main/java/com/revworkforce/employee/dto/TeamMemberResponse.java
- employee-service/src/main/java/com/revworkforce/employee/dto/EmployeeSummaryResponse.java
- employee-service/src/main/java/com/revworkforce/employee/dto/ManagerAssignRequest.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 4: Add repository

**What**: Create JPA repository for employee data access.

```bash
# Add repository file
git add employee-service/src/main/java/com/revworkforce/employee/repository/

# Commit
git commit -m "$(cat <<'EOF'
feat(employee): Add EmployeeRepository

- Create EmployeeRepository extending JpaRepository
- Add findByUserId method for auth integration
- Add findByManagerEmployeeId for team queries
- Add search methods by name, email, department
- Add findByIsActive for active employee queries

Repository:
- EmployeeRepository: Employee data access with custom queries

Files:
- employee-service/src/main/java/com/revworkforce/employee/repository/EmployeeRepository.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 5: Add service layer

**What**: Implement business logic for employee and directory operations.

```bash
# Add all service files
git add employee-service/src/main/java/com/revworkforce/employee/service/

# Commit
git commit -m "$(cat <<'EOF'
feat(employee): Add EmployeeService and DirectoryService

- Create EmployeeService for employee CRUD operations
- Implement DirectoryService for search and directory features
- Add createEmployee method (admin only)
- Add updateProfile for self-service updates
- Implement manager assignment logic
- Add employee activation/deactivation
- Implement search by name, email, department
- Add getTeamMembers for managers
- Add internal methods for other services

Services:
- EmployeeService: Core employee management logic
- DirectoryService: Employee search and directory features

Key Features:
- Employee profile CRUD
- Manager-reportee relationship
- Employee search and directory
- Active/inactive status management
- Integration with auth-service via Feign

Files:
- employee-service/src/main/java/com/revworkforce/employee/service/EmployeeService.java
- employee-service/src/main/java/com/revworkforce/employee/service/DirectoryService.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 6: Add REST controllers

**What**: Create REST endpoints for employee and directory operations.

```bash
# Add all controller files
git add employee-service/src/main/java/com/revworkforce/employee/controller/

# Commit
git commit -m "$(cat <<'EOF'
feat(employee): Add EmployeeController, DirectoryController, InternalController

- POST /api/employees - Create employee (admin only)
- GET /api/employees/profile - Get current employee profile
- PUT /api/employees/profile - Update own profile
- GET /api/employees - Get all employees (admin only)
- GET /api/employees/{id} - Get employee by ID
- PATCH /api/employees/{id}/status - Activate/deactivate employee
- PATCH /api/employees/{id}/manager/{managerId} - Assign manager
- GET /api/employees/search - Search employees
- GET /api/employees/team - Get manager's team members
- GET /api/internal/employees/{id} - Internal endpoint for services
- GET /api/internal/employees/batch - Get multiple employees

Controllers:
- EmployeeController: Employee CRUD endpoints
- DirectoryController: Search and directory endpoints
- InternalController: Internal endpoints for other services

Files:
- employee-service/src/main/java/com/revworkforce/employee/controller/EmployeeController.java
- employee-service/src/main/java/com/revworkforce/employee/controller/DirectoryController.java
- employee-service/src/main/java/com/revworkforce/employee/controller/InternalController.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 7: Add configuration classes

**What**: Configure Security, Feign clients, and CORS.

```bash
# Add all config files
git add employee-service/src/main/java/com/revworkforce/employee/config/

# Commit
git commit -m "$(cat <<'EOF'
feat(employee): Add SecurityConfig and FeignConfig

- Create SecurityConfig with stateless security
- Configure public endpoints and protected endpoints
- Add FeignConfig for auth-service client
- Create AuthServiceClient Feign interface
- Implement token validation via auth-service
- Add CORS configuration

Configuration:
- SecurityConfig: Spring Security with JWT validation
- FeignConfig: Feign client configuration
- AuthServiceClient: Feign client for auth-service integration

Feign Client Endpoints:
- GET /api/auth/validate - Validate JWT token
- GET /api/auth/user/{userId} - Get user details

Files:
- employee-service/src/main/java/com/revworkforce/employee/config/SecurityConfig.java
- employee-service/src/main/java/com/revworkforce/employee/config/FeignConfig.java
- employee-service/src/main/java/com/revworkforce/employee/client/AuthServiceClient.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 8: Add exception handling

**What**: Create custom exceptions and global exception handler.

```bash
# Add exception files
git add employee-service/src/main/java/com/revworkforce/employee/exception/

# Commit
git commit -m "$(cat <<'EOF'
feat(employee): Add exception handling

- Create ResourceNotFoundException for employee not found
- Add UnauthorizedException for access denied
- Create InvalidRequestException for validation errors
- Add GlobalExceptionHandler with @RestControllerAdvice
- Implement ErrorResponse DTO for consistent error format

Exceptions:
- ResourceNotFoundException: HTTP 404
- UnauthorizedException: HTTP 403
- InvalidRequestException: HTTP 400

Files:
- employee-service/src/main/java/com/revworkforce/employee/exception/ResourceNotFoundException.java
- employee-service/src/main/java/com/revworkforce/employee/exception/UnauthorizedException.java
- employee-service/src/main/java/com/revworkforce/employee/exception/InvalidRequestException.java
- employee-service/src/main/java/com/revworkforce/employee/exception/GlobalExceptionHandler.java
- employee-service/src/main/java/com/revworkforce/employee/exception/ErrorResponse.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

**CHECKPOINT**: employee-service is complete. Leave and performance services can now integrate.

---

## Krishna Babu - admin-service

**Responsibility**: Admin operations, master data, and notification management.

### Pre-requisites

```bash
# Configure Git with your identity (should already be configured)
git config --global user.name "Krishna Babu"
git config --global user.email "krishna.babu@revature.com"

# Navigate to the project root
cd /Users/vimalkrishnan/Workspace/revature/2353/review/p2/repos/p3-revworkforce

# Ensure you have the latest changes
git pull origin main
```

### Commit 1: Setup admin-service project structure

**What**: Create the basic project structure with pom.xml, application.yml, and main application class.

```bash
# Add setup files
git add admin-service/pom.xml \
        admin-service/src/main/resources/application.yml \
        admin-service/src/main/java/com/revworkforce/admin/AdminServiceApplication.java

# Commit
git commit -m "$(cat <<'EOF'
feat(admin): Setup admin-service project structure

- Add pom.xml with Spring Boot, JPA, H2, Feign dependencies
- Configure application.yml with port 8085, H2 database, Eureka client
- Create AdminServiceApplication.java main class
- Enable Eureka client and Feign clients

Service: admin-service
Port: 8085

Files:
- admin-service/pom.xml
- admin-service/src/main/resources/application.yml
- admin-service/src/main/java/com/revworkforce/admin/AdminServiceApplication.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 2: Add entities for master data and notifications

**What**: Create Department, Designation, Holiday, Announcement, and AdminNotification entities.

```bash
# Add all entity files
git add admin-service/src/main/java/com/revworkforce/admin/entity/

# Commit
git commit -m "$(cat <<'EOF'
feat(admin): Add entities for master data and notifications

- Create Department entity with departmentId and departmentName
- Add Designation entity with designationId and designationName
- Create Holiday entity with holidayId, holidayName, holidayDate, description
- Add Announcement entity with announcementId, title, content, createdBy, createdAt
- Create AdminNotification entity for notification management
- Configure JPA annotations and auto-increment IDs

Entities:
- Department: Organizational departments
- Designation: Job titles and positions
- Holiday: Company holidays
- Announcement: Company announcements
- AdminNotification: Admin notification system

Files:
- admin-service/src/main/java/com/revworkforce/admin/entity/Department.java
- admin-service/src/main/java/com/revworkforce/admin/entity/Designation.java
- admin-service/src/main/java/com/revworkforce/admin/entity/Holiday.java
- admin-service/src/main/java/com/revworkforce/admin/entity/Announcement.java
- admin-service/src/main/java/com/revworkforce/admin/entity/AdminNotification.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 3: Add DTOs for admin operations

**What**: Create all DTOs for master data and notification operations.

```bash
# Add all DTO files
git add admin-service/src/main/java/com/revworkforce/admin/dto/

# Commit
git commit -m "$(cat <<'EOF'
feat(admin): Add DTOs for admin operations

- Create DepartmentRequest and DepartmentResponse
- Add DesignationRequest and DesignationResponse
- Create HolidayRequest and HolidayResponse
- Add AnnouncementRequest and AnnouncementResponse
- Create AdminNotificationRequest and AdminNotificationResponse
- Add NotificationSendRequest for sending notifications
- Include validation annotations

DTOs:
- DepartmentRequest/Response: Department CRUD
- DesignationRequest/Response: Designation CRUD
- HolidayRequest/Response: Holiday management
- AnnouncementRequest/Response: Announcement management
- AdminNotificationRequest/Response: Notification management
- NotificationSendRequest: Send notifications to users

Files:
- admin-service/src/main/java/com/revworkforce/admin/dto/DepartmentRequest.java
- admin-service/src/main/java/com/revworkforce/admin/dto/DepartmentResponse.java
- admin-service/src/main/java/com/revworkforce/admin/dto/DesignationRequest.java
- admin-service/src/main/java/com/revworkforce/admin/dto/DesignationResponse.java
- admin-service/src/main/java/com/revworkforce/admin/dto/HolidayRequest.java
- admin-service/src/main/java/com/revworkforce/admin/dto/HolidayResponse.java
- admin-service/src/main/java/com/revworkforce/admin/dto/AnnouncementRequest.java
- admin-service/src/main/java/com/revworkforce/admin/dto/AnnouncementResponse.java
- admin-service/src/main/java/com/revworkforce/admin/dto/AdminNotificationRequest.java
- admin-service/src/main/java/com/revworkforce/admin/dto/AdminNotificationResponse.java
- admin-service/src/main/java/com/revworkforce/admin/dto/NotificationSendRequest.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 4: Add repositories

**What**: Create JPA repositories for all entities.

```bash
# Add all repository files
git add admin-service/src/main/java/com/revworkforce/admin/repository/

# Commit
git commit -m "$(cat <<'EOF'
feat(admin): Add repositories for master data and notifications

- Create DepartmentRepository with findByDepartmentName
- Add DesignationRepository with findByDesignationName
- Create HolidayRepository with findByHolidayDateBetween
- Add AnnouncementRepository with custom queries
- Create AdminNotificationRepository

Repositories:
- DepartmentRepository: Department data access
- DesignationRepository: Designation data access
- HolidayRepository: Holiday data access with date range queries
- AnnouncementRepository: Announcement data access
- AdminNotificationRepository: Notification data access

Files:
- admin-service/src/main/java/com/revworkforce/admin/repository/DepartmentRepository.java
- admin-service/src/main/java/com/revworkforce/admin/repository/DesignationRepository.java
- admin-service/src/main/java/com/revworkforce/admin/repository/HolidayRepository.java
- admin-service/src/main/java/com/revworkforce/admin/repository/AnnouncementRepository.java
- admin-service/src/main/java/com/revworkforce/admin/repository/AdminNotificationRepository.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 5: Add service layer

**What**: Implement business logic for all admin operations.

```bash
# Add all service files
git add admin-service/src/main/java/com/revworkforce/admin/service/

# Commit
git commit -m "$(cat <<'EOF'
feat(admin): Add service layer for admin operations

- Create DepartmentService for department CRUD
- Add DesignationService for designation CRUD
- Create HolidayService for holiday management
- Add AnnouncementService for announcement CRUD
- Create AdminNotificationService for notification management
- Implement validation and duplicate checking
- Add methods for internal endpoints

Services:
- DepartmentService: Department management
- DesignationService: Designation management
- HolidayService: Holiday management with date validation
- AnnouncementService: Announcement CRUD with created/updated tracking
- AdminNotificationService: Notification creation and delivery

Key Features:
- Master data CRUD operations
- Duplicate prevention for departments and designations
- Holiday date validation
- Announcement timestamp tracking
- Notification management

Files:
- admin-service/src/main/java/com/revworkforce/admin/service/DepartmentService.java
- admin-service/src/main/java/com/revworkforce/admin/service/DesignationService.java
- admin-service/src/main/java/com/revworkforce/admin/service/HolidayService.java
- admin-service/src/main/java/com/revworkforce/admin/service/AnnouncementService.java
- admin-service/src/main/java/com/revworkforce/admin/service/AdminNotificationService.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 6: Add REST controllers

**What**: Create REST endpoints for admin operations.

```bash
# Add all controller files
git add admin-service/src/main/java/com/revworkforce/admin/controller/

# Commit
git commit -m "$(cat <<'EOF'
feat(admin): Add AdminController, MasterDataController, InternalController

- GET/POST/DELETE /api/admin/departments - Department CRUD
- GET/POST/DELETE /api/admin/designations - Designation CRUD
- GET/POST/DELETE /api/admin/holidays - Holiday CRUD
- GET/POST/PUT/DELETE /api/admin/announcements - Announcement CRUD
- GET /api/admin/notifications - Get notifications
- POST /api/admin/notifications/send - Send notification
- PATCH /api/admin/notifications/{id}/read - Mark as read
- GET /api/internal/departments - Internal endpoint
- GET /api/internal/designations - Internal endpoint
- GET /api/internal/holidays - Internal endpoint

Controllers:
- AdminController: Admin-only master data operations
- MasterDataController: Public master data viewing
- InternalController: Internal endpoints for other services

Files:
- admin-service/src/main/java/com/revworkforce/admin/controller/AdminController.java
- admin-service/src/main/java/com/revworkforce/admin/controller/MasterDataController.java
- admin-service/src/main/java/com/revworkforce/admin/controller/InternalController.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 7: Add configuration classes

**What**: Configure Security, Feign clients, and data loading.

```bash
# Add all config files
git add admin-service/src/main/java/com/revworkforce/admin/config/

# Commit
git commit -m "$(cat <<'EOF'
feat(admin): Add SecurityConfig, FeignConfig, and DataLoader

- Create SecurityConfig with admin authorization
- Configure protected admin endpoints (POST, PUT, DELETE)
- Add FeignConfig for auth-service client
- Create AuthServiceClient Feign interface
- Implement DataLoader for initial master data seeding
- Add CORS configuration

Configuration:
- SecurityConfig: Spring Security with admin role checks
- FeignConfig: Feign client configuration
- DataLoader: Seeds departments, designations, and holidays
- AuthServiceClient: Feign client for auth-service

Default Master Data:
- Departments: Engineering, HR, Finance, Sales, Marketing
- Designations: Software Engineer, Senior Engineer, Manager, Director
- Holidays: New Year, Independence Day, Christmas

Files:
- admin-service/src/main/java/com/revworkforce/admin/config/SecurityConfig.java
- admin-service/src/main/java/com/revworkforce/admin/config/FeignConfig.java
- admin-service/src/main/java/com/revworkforce/admin/config/DataLoader.java
- admin-service/src/main/java/com/revworkforce/admin/client/AuthServiceClient.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 8: Add exception handling

**What**: Create custom exceptions and global exception handler.

```bash
# Add exception files
git add admin-service/src/main/java/com/revworkforce/admin/exception/

# Commit
git commit -m "$(cat <<'EOF'
feat(admin): Add exception handling

- Create ResourceNotFoundException for entity not found
- Add DuplicateResourceException for duplicate entries
- Create UnauthorizedException for access denied
- Add InvalidRequestException for validation errors
- Implement GlobalExceptionHandler with @RestControllerAdvice
- Add ErrorResponse DTO for consistent error format

Exceptions:
- ResourceNotFoundException: HTTP 404
- DuplicateResourceException: HTTP 409
- UnauthorizedException: HTTP 403
- InvalidRequestException: HTTP 400

Files:
- admin-service/src/main/java/com/revworkforce/admin/exception/ResourceNotFoundException.java
- admin-service/src/main/java/com/revworkforce/admin/exception/DuplicateResourceException.java
- admin-service/src/main/java/com/revworkforce/admin/exception/UnauthorizedException.java
- admin-service/src/main/java/com/revworkforce/admin/exception/InvalidRequestException.java
- admin-service/src/main/java/com/revworkforce/admin/exception/GlobalExceptionHandler.java
- admin-service/src/main/java/com/revworkforce/admin/exception/ErrorResponse.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

**CHECKPOINT**: admin-service is complete. Krishna Babu has finished both infrastructure and admin-service.

---

## Sai Krishna - leave-service

**Responsibility**: Leave management and leave notification service.

### Pre-requisites

```bash
# Configure Git with your identity
git config --global user.name "Sai Krishna"
git config --global user.email "sai.krishna@revature.com"

# Navigate to the project root
cd /Users/vimalkrishnan/Workspace/revature/2353/review/p2/repos/p3-revworkforce

# Ensure you have the latest changes (auth and employee services)
git pull origin main
```

### Commit 1: Setup leave-service project structure

**What**: Create the basic project structure with pom.xml, application.yml, and main application class.

```bash
# Add setup files
git add leave-service/pom.xml \
        leave-service/src/main/resources/application.yml \
        leave-service/src/main/java/com/revworkforce/leave/LeaveServiceApplication.java

# Commit
git commit -m "$(cat <<'EOF'
feat(leave): Setup leave-service project structure

- Add pom.xml with Spring Boot, JPA, H2, Feign dependencies
- Configure application.yml with port 8083, H2 database, Eureka client
- Create LeaveServiceApplication.java main class
- Enable Eureka client and Feign clients

Service: leave-service
Port: 8083

Files:
- leave-service/pom.xml
- leave-service/src/main/resources/application.yml
- leave-service/src/main/java/com/revworkforce/leave/LeaveServiceApplication.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 2: Add entities and enums

**What**: Create LeaveRequest, LeaveBalance, LeaveNotification entities and enums.

```bash
# Add entity and enum files
git add leave-service/src/main/java/com/revworkforce/leave/entity/ \
        leave-service/src/main/java/com/revworkforce/leave/enums/

# Commit
git commit -m "$(cat <<'EOF'
feat(leave): Add entities and enums for leave management

- Create LeaveRequest entity with leaveId, userId, startDate, endDate
- Add LeaveBalance entity for tracking leave balances
- Create LeaveNotification entity for leave notifications
- Add LeaveType enum: SICK, PERSONAL, VACATION, MATERNITY, PATERNITY
- Add LeaveStatus enum: PENDING, APPROVED, REJECTED, CANCELED
- Configure JPA annotations and relationships

Entities:
- LeaveRequest: Leave application with dates, type, status, reason
- LeaveBalance: Employee leave balance tracking (24 days default)
- LeaveNotification: Leave-related notifications

Enums:
- LeaveType: Types of leave
- LeaveStatus: Leave request status

Files:
- leave-service/src/main/java/com/revworkforce/leave/entity/LeaveRequest.java
- leave-service/src/main/java/com/revworkforce/leave/entity/LeaveBalance.java
- leave-service/src/main/java/com/revworkforce/leave/entity/LeaveNotification.java
- leave-service/src/main/java/com/revworkforce/leave/enums/LeaveType.java
- leave-service/src/main/java/com/revworkforce/leave/enums/LeaveStatus.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 3: Add DTOs for leave operations

**What**: Create all DTOs for leave management and notifications.

```bash
# Add all DTO files
git add leave-service/src/main/java/com/revworkforce/leave/dto/

# Commit
git commit -m "$(cat <<'EOF'
feat(leave): Add DTOs for leave operations

- Create LeaveApplyRequest for leave application
- Add LeaveDecisionRequest for approve/reject
- Create LeaveResponse for leave details
- Add LeaveSummaryResponse for leave balance
- Create TeamLeaveResponse for manager's team view
- Add LeaveNotificationResponse for notifications
- Include validation annotations

DTOs:
- LeaveApplyRequest: startDate, endDate, leaveType, reason
- LeaveDecisionRequest: decision, comment
- LeaveResponse: Complete leave details with status
- LeaveSummaryResponse: Leave balance and usage summary
- TeamLeaveResponse: Team member leave info for managers
- LeaveNotificationResponse: Leave notification details

Files:
- leave-service/src/main/java/com/revworkforce/leave/dto/LeaveApplyRequest.java
- leave-service/src/main/java/com/revworkforce/leave/dto/LeaveDecisionRequest.java
- leave-service/src/main/java/com/revworkforce/leave/dto/LeaveResponse.java
- leave-service/src/main/java/com/revworkforce/leave/dto/LeaveSummaryResponse.java
- leave-service/src/main/java/com/revworkforce/leave/dto/TeamLeaveResponse.java
- leave-service/src/main/java/com/revworkforce/leave/dto/LeaveNotificationResponse.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 4: Add repositories

**What**: Create JPA repositories for leave data access.

```bash
# Add all repository files
git add leave-service/src/main/java/com/revworkforce/leave/repository/

# Commit
git commit -m "$(cat <<'EOF'
feat(leave): Add repositories for leave management

- Create LeaveRequestRepository with findByUserId
- Add LeaveBalanceRepository with findByUserId
- Create LeaveNotificationRepository with notification queries
- Add methods for finding leaves by status, date range
- Add manager team leave queries

Repositories:
- LeaveRequestRepository: Leave request data access
- LeaveBalanceRepository: Leave balance tracking
- LeaveNotificationRepository: Leave notification management

Files:
- leave-service/src/main/java/com/revworkforce/leave/repository/LeaveRequestRepository.java
- leave-service/src/main/java/com/revworkforce/leave/repository/LeaveBalanceRepository.java
- leave-service/src/main/java/com/revworkforce/leave/repository/LeaveNotificationRepository.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 5: Add service layer

**What**: Implement business logic for leave operations.

```bash
# Add all service files
git add leave-service/src/main/java/com/revworkforce/leave/service/

# Commit
git commit -m "$(cat <<'EOF'
feat(leave): Add service layer for leave operations

- Create LeaveService for leave CRUD and workflow
- Implement LeaveBalanceService for balance calculation
- Add LeaveNotificationService for notifications
- Implement leave application with validation
- Add leave approval/rejection workflow
- Implement leave cancellation logic
- Add leave balance checking and deduction
- Implement team leave view for managers
- Add manager notification on leave application
- Integrate with employee-service via Feign

Services:
- LeaveService: Core leave management logic
- LeaveBalanceService: Leave balance calculation and tracking
- LeaveNotificationService: Leave notification management

Key Features:
- Leave application with balance validation
- Leave approval workflow (manager/admin only)
- Leave cancellation (only for pending/approved)
- Leave balance tracking (24 days default)
- Manager notifications
- Team leave view

Files:
- leave-service/src/main/java/com/revworkforce/leave/service/LeaveService.java
- leave-service/src/main/java/com/revworkforce/leave/service/LeaveBalanceService.java
- leave-service/src/main/java/com/revworkforce/leave/service/LeaveNotificationService.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 6: Add REST controllers

**What**: Create REST endpoints for leave operations.

```bash
# Add all controller files
git add leave-service/src/main/java/com/revworkforce/leave/controller/

# Commit
git commit -m "$(cat <<'EOF'
feat(leave): Add LeaveController and LeaveNotificationController

- POST /api/leaves - Apply for leave
- GET /api/leaves/my - Get user's leave requests
- GET /api/leaves/summary - Get leave balance summary
- GET /api/leaves/team - Get team's leaves (manager only)
- PATCH /api/leaves/{id}/cancel - Cancel leave request
- PATCH /api/leaves/{id}/approve - Approve leave (manager/admin)
- PATCH /api/leaves/{id}/reject - Reject leave (manager/admin)
- GET /api/leaves/notifications - Get leave notifications
- PATCH /api/leaves/notifications/{id}/read - Mark notification read

Controllers:
- LeaveController: Leave management endpoints
- LeaveNotificationController: Notification endpoints

Files:
- leave-service/src/main/java/com/revworkforce/leave/controller/LeaveController.java
- leave-service/src/main/java/com/revworkforce/leave/controller/LeaveNotificationController.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 7: Add configuration classes

**What**: Configure Security, Feign clients, and data loading.

```bash
# Add all config files
git add leave-service/src/main/java/com/revworkforce/leave/config/

# Commit
git commit -m "$(cat <<'EOF'
feat(leave): Add SecurityConfig, FeignConfig, and DataLoader

- Create SecurityConfig with role-based access
- Configure manager-only endpoints for approval/rejection
- Add FeignConfig for employee-service client
- Create EmployeeServiceClient Feign interface
- Implement DataLoader for initial leave balance setup
- Add CORS configuration

Configuration:
- SecurityConfig: Spring Security with role-based authorization
- FeignConfig: Feign client configuration
- DataLoader: Seeds initial leave balances for employees
- EmployeeServiceClient: Feign client for employee-service

Feign Client Endpoints:
- GET /api/internal/employees/{id} - Get employee details
- Retrieve manager information for approval workflow

Files:
- leave-service/src/main/java/com/revworkforce/leave/config/SecurityConfig.java
- leave-service/src/main/java/com/revworkforce/leave/config/FeignConfig.java
- leave-service/src/main/java/com/revworkforce/leave/config/DataLoader.java
- leave-service/src/main/java/com/revworkforce/leave/client/EmployeeServiceClient.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 8: Add exception handling

**What**: Create custom exceptions and global exception handler.

```bash
# Add exception files
git add leave-service/src/main/java/com/revworkforce/leave/exception/

# Commit
git commit -m "$(cat <<'EOF'
feat(leave): Add exception handling

- Create ResourceNotFoundException for leave not found
- Add InsufficientLeaveBalanceException for balance errors
- Create InvalidLeaveStatusException for status errors
- Add UnauthorizedException for access denied
- Implement GlobalExceptionHandler with @RestControllerAdvice
- Add ErrorResponse DTO for consistent error format

Exceptions:
- ResourceNotFoundException: HTTP 404
- InsufficientLeaveBalanceException: HTTP 400
- InvalidLeaveStatusException: HTTP 400
- UnauthorizedException: HTTP 403

Files:
- leave-service/src/main/java/com/revworkforce/leave/exception/ResourceNotFoundException.java
- leave-service/src/main/java/com/revworkforce/leave/exception/InsufficientLeaveBalanceException.java
- leave-service/src/main/java/com/revworkforce/leave/exception/InvalidLeaveStatusException.java
- leave-service/src/main/java/com/revworkforce/leave/exception/UnauthorizedException.java
- leave-service/src/main/java/com/revworkforce/leave/exception/GlobalExceptionHandler.java
- leave-service/src/main/java/com/revworkforce/leave/exception/ErrorResponse.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

**CHECKPOINT**: leave-service is complete.

---

## Saideep Reddy - performance-service

**Responsibility**: Performance reviews, goals, and feedback management.

### Pre-requisites

```bash
# Configure Git with your identity
git config --global user.name "Saideep Reddy"
git config --global user.email "saideep.reddy@revature.com"

# Navigate to the project root
cd /Users/vimalkrishnan/Workspace/revature/2353/review/p2/repos/p3-revworkforce

# Ensure you have the latest changes (auth and employee services)
git pull origin main
```

### Commit 1: Setup performance-service project structure

**What**: Create the basic project structure with pom.xml, application.yml, and main application class.

```bash
# Add setup files
git add performance-service/pom.xml \
        performance-service/src/main/resources/application.yml \
        performance-service/src/main/java/com/revworkforce/performance/PerformanceServiceApplication.java

# Commit
git commit -m "$(cat <<'EOF'
feat(performance): Setup performance-service project structure

- Add pom.xml with Spring Boot, JPA, H2, Feign dependencies
- Configure application.yml with port 8084, H2 database, Eureka client
- Create PerformanceServiceApplication.java main class
- Enable Eureka client and Feign clients

Service: performance-service
Port: 8084

Files:
- performance-service/pom.xml
- performance-service/src/main/resources/application.yml
- performance-service/src/main/java/com/revworkforce/performance/PerformanceServiceApplication.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 2: Add entities and enums

**What**: Create PerformanceReview, Goal, PerformanceNotification entities and enums.

```bash
# Add entity and enum files
git add performance-service/src/main/java/com/revworkforce/performance/entity/ \
        performance-service/src/main/java/com/revworkforce/performance/enums/

# Commit
git commit -m "$(cat <<'EOF'
feat(performance): Add entities and enums for performance management

- Create PerformanceReview entity with reviewId, userId, reviewerId
- Add Goal entity with goalId, userId, title, description, deadline
- Create PerformanceNotification entity for notifications
- Add ReviewStatus enum: DRAFT, REVIEWED
- Add GoalStatus enum: NOT_STARTED, IN_PROGRESS, COMPLETED
- Add GoalPriority enum: HIGH, MEDIUM, LOW
- Configure JPA annotations and relationships

Entities:
- PerformanceReview: Performance review with ratings and feedback
- Goal: Employee goals with status and priority
- PerformanceNotification: Performance-related notifications

Enums:
- ReviewStatus: Review workflow status
- GoalStatus: Goal completion status
- GoalPriority: Goal priority levels

Files:
- performance-service/src/main/java/com/revworkforce/performance/entity/PerformanceReview.java
- performance-service/src/main/java/com/revworkforce/performance/entity/Goal.java
- performance-service/src/main/java/com/revworkforce/performance/entity/PerformanceNotification.java
- performance-service/src/main/java/com/revworkforce/performance/enums/ReviewStatus.java
- performance-service/src/main/java/com/revworkforce/performance/enums/GoalStatus.java
- performance-service/src/main/java/com/revworkforce/performance/enums/GoalPriority.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 3: Add DTOs for performance operations

**What**: Create all DTOs for performance reviews and goals.

```bash
# Add all DTO files
git add performance-service/src/main/java/com/revworkforce/performance/dto/

# Commit
git commit -m "$(cat <<'EOF'
feat(performance): Add DTOs for performance operations

- Create PerformanceReviewRequest for creating reviews
- Add PerformanceReviewResponse for review details
- Create ReviewFeedbackRequest for manager feedback
- Add TeamReviewResponse for team view
- Create GoalRequest for goal creation/update
- Add GoalResponse for goal details
- Create GoalStatusUpdateRequest for status changes
- Add TeamGoalResponse for team goals view
- Include validation annotations

DTOs:
- PerformanceReviewRequest: userId, reviewPeriod, ratings
- PerformanceReviewResponse: Complete review details
- ReviewFeedbackRequest: feedback, ratings
- TeamReviewResponse: Team member reviews for managers
- GoalRequest: title, description, deadline, priority
- GoalResponse: Complete goal details
- GoalStatusUpdateRequest: status, comment
- TeamGoalResponse: Team member goals

Files:
- performance-service/src/main/java/com/revworkforce/performance/dto/PerformanceReviewRequest.java
- performance-service/src/main/java/com/revworkforce/performance/dto/PerformanceReviewResponse.java
- performance-service/src/main/java/com/revworkforce/performance/dto/ReviewFeedbackRequest.java
- performance-service/src/main/java/com/revworkforce/performance/dto/TeamReviewResponse.java
- performance-service/src/main/java/com/revworkforce/performance/dto/GoalRequest.java
- performance-service/src/main/java/com/revworkforce/performance/dto/GoalResponse.java
- performance-service/src/main/java/com/revworkforce/performance/dto/GoalStatusUpdateRequest.java
- performance-service/src/main/java/com/revworkforce/performance/dto/TeamGoalResponse.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 4: Add repositories

**What**: Create JPA repositories for performance data access.

```bash
# Add all repository files
git add performance-service/src/main/java/com/revworkforce/performance/repository/

# Commit
git commit -m "$(cat <<'EOF'
feat(performance): Add repositories for performance management

- Create PerformanceReviewRepository with findByUserId
- Add GoalRepository with findByUserId and status queries
- Create PerformanceNotificationRepository
- Add methods for finding reviews by status, date range
- Add manager team review and goal queries

Repositories:
- PerformanceReviewRepository: Performance review data access
- GoalRepository: Goal data access with status queries
- PerformanceNotificationRepository: Notification management

Files:
- performance-service/src/main/java/com/revworkforce/performance/repository/PerformanceReviewRepository.java
- performance-service/src/main/java/com/revworkforce/performance/repository/GoalRepository.java
- performance-service/src/main/java/com/revworkforce/performance/repository/PerformanceNotificationRepository.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 5: Add service layer

**What**: Implement business logic for performance reviews and goals.

```bash
# Add all service files
git add performance-service/src/main/java/com/revworkforce/performance/service/

# Commit
git commit -m "$(cat <<'EOF'
feat(performance): Add service layer for performance operations

- Create PerformanceService for review CRUD and workflow
- Implement GoalService for goal management
- Add PerformanceNotificationService for notifications
- Implement review creation (manager/admin only)
- Add review feedback workflow
- Implement goal CRUD operations
- Add goal status tracking
- Implement team review/goal views for managers
- Add employee notification on review creation
- Integrate with employee-service via Feign

Services:
- PerformanceService: Performance review management
- GoalService: Goal tracking and management
- PerformanceNotificationService: Notification handling

Key Features:
- Performance review creation by managers
- Review feedback with ratings
- Goal creation and tracking
- Goal status updates (NOT_STARTED, IN_PROGRESS, COMPLETED)
- Goal priority management
- Team views for managers
- Employee notifications

Files:
- performance-service/src/main/java/com/revworkforce/performance/service/PerformanceService.java
- performance-service/src/main/java/com/revworkforce/performance/service/GoalService.java
- performance-service/src/main/java/com/revworkforce/performance/service/PerformanceNotificationService.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 6: Add REST controllers

**What**: Create REST endpoints for performance and goal operations.

```bash
# Add all controller files
git add performance-service/src/main/java/com/revworkforce/performance/controller/

# Commit
git commit -m "$(cat <<'EOF'
feat(performance): Add PerformanceController and GoalController

- POST /api/performance - Create performance review (manager/admin)
- GET /api/performance/my - Get user's reviews
- GET /api/performance/team - Get team reviews (manager)
- PATCH /api/performance/{id}/feedback - Provide feedback (manager)
- POST /api/goals - Create goal
- GET /api/goals/my - Get user's goals
- GET /api/goals/team - Get team goals (manager)
- PATCH /api/goals/{id}/status - Update goal status
- PATCH /api/goals/{id}/comment - Add manager comment

Controllers:
- PerformanceController: Performance review endpoints
- GoalController: Goal management endpoints

Files:
- performance-service/src/main/java/com/revworkforce/performance/controller/PerformanceController.java
- performance-service/src/main/java/com/revworkforce/performance/controller/GoalController.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 7: Add configuration classes

**What**: Configure Security, Feign clients, and CORS.

```bash
# Add all config files
git add performance-service/src/main/java/com/revworkforce/performance/config/

# Commit
git commit -m "$(cat <<'EOF'
feat(performance): Add SecurityConfig and FeignConfig

- Create SecurityConfig with role-based access
- Configure manager-only endpoints for review creation
- Add FeignConfig for employee-service client
- Create EmployeeServiceClient Feign interface
- Add CORS configuration

Configuration:
- SecurityConfig: Spring Security with manager/admin authorization
- FeignConfig: Feign client configuration
- EmployeeServiceClient: Feign client for employee-service

Feign Client Endpoints:
- GET /api/internal/employees/{id} - Get employee details
- Retrieve manager information for authorization

Files:
- performance-service/src/main/java/com/revworkforce/performance/config/SecurityConfig.java
- performance-service/src/main/java/com/revworkforce/performance/config/FeignConfig.java
- performance-service/src/main/java/com/revworkforce/performance/client/EmployeeServiceClient.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Commit 8: Add exception handling

**What**: Create custom exceptions and global exception handler.

```bash
# Add exception files
git add performance-service/src/main/java/com/revworkforce/performance/exception/

# Commit
git commit -m "$(cat <<'EOF'
feat(performance): Add exception handling

- Create ResourceNotFoundException for review/goal not found
- Add UnauthorizedException for access denied
- Create InvalidRequestException for validation errors
- Add InvalidStatusException for status errors
- Implement GlobalExceptionHandler with @RestControllerAdvice
- Add ErrorResponse DTO for consistent error format

Exceptions:
- ResourceNotFoundException: HTTP 404
- UnauthorizedException: HTTP 403
- InvalidRequestException: HTTP 400
- InvalidStatusException: HTTP 400

Files:
- performance-service/src/main/java/com/revworkforce/performance/exception/ResourceNotFoundException.java
- performance-service/src/main/java/com/revworkforce/performance/exception/UnauthorizedException.java
- performance-service/src/main/java/com/revworkforce/performance/exception/InvalidRequestException.java
- performance-service/src/main/java/com/revworkforce/performance/exception/InvalidStatusException.java
- performance-service/src/main/java/com/revworkforce/performance/exception/GlobalExceptionHandler.java
- performance-service/src/main/java/com/revworkforce/performance/exception/ErrorResponse.java

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

**CHECKPOINT**: performance-service is complete.

---

## Final Steps - All Developers

After all commits are completed:

### 1. Verify All Services

```bash
# Navigate to project root
cd /Users/vimalkrishnan/Workspace/revature/2353/review/p2/repos/p3-revworkforce

# Check git status (should be clean)
git status

# View commit history
git log --oneline --graph --all

# Count commits per author
git shortlog -sn
```

### 2. Build All Services

```bash
# Build all services from parent
mvn clean install

# Or build individually
cd config-server && mvn clean install && cd ..
cd discovery-server && mvn clean install && cd ..
cd api-gateway && mvn clean install && cd ..
cd auth-service && mvn clean install && cd ..
cd employee-service && mvn clean install && cd ..
cd leave-service && mvn clean install && cd ..
cd performance-service && mvn clean install && cd ..
cd admin-service && mvn clean install && cd ..
```

### 3. Run with Docker Compose

```bash
# Start all services
docker-compose up -d

# Check service status
docker-compose ps

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

### 4. Test Services

```bash
# Check Eureka Dashboard
open http://localhost:8761

# Test Auth Service
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@revature.com","password":"admin123"}'

# Test Employee Service (requires JWT token)
curl -X GET http://localhost:8080/api/employees/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## Commit Summary by Developer

| Developer | Service(s) | Expected Commits |
|-----------|-----------|------------------|
| **Krishna Babu** | Infrastructure + admin-service | 14 commits (6 infrastructure + 8 admin) |
| **Ganesh** | auth-service | 8 commits |
| **Vamsi** | employee-service | 8 commits |
| **Sai Krishna** | leave-service | 8 commits |
| **Saideep Reddy** | performance-service | 8 commits |
| **TOTAL** | All services | **46 commits** |

---

## Important Notes

1. **Commit Order**: Follow the exact commit order specified. Krishna Babu MUST complete infrastructure before others start.

2. **Git Pull**: Always `git pull origin main` before starting your commits to get the latest changes.

3. **Commit Messages**: Copy the commit messages exactly as provided. They follow conventional commit format.

4. **File Paths**: All file paths are absolute. Ensure you're in the correct directory before running git commands.

5. **Feature Branches**: Optional - You can create feature branches, but this guide assumes direct commits to main.

6. **Testing**: Test your service locally before committing. Ensure the service starts without errors.

7. **Dependencies**: Services have dependencies. Don't start a service until its dependencies are committed:
   - employee-service depends on auth-service
   - leave-service depends on auth-service and employee-service
   - performance-service depends on auth-service and employee-service

8. **Port Conflicts**: Ensure no other services are running on ports 8080-8085, 8761, 8888.

9. **Java Version**: Ensure Java 17 is installed and JAVA_HOME is set correctly.

10. **Maven**: Ensure Maven 3.8+ is installed.

---

## Troubleshooting

### Issue: Port already in use
```bash
# Find process using port
lsof -i :8080

# Kill process
kill -9 PID
```

### Issue: Service not registering with Eureka
- Wait 30-60 seconds for registration
- Check application.yml for correct eureka.client configuration
- Ensure discovery-server is running

### Issue: Feign client connection refused
- Ensure the target service is running
- Check service name in @FeignClient annotation
- Verify Eureka registration

### Issue: Git merge conflicts
- Pull latest changes: `git pull origin main`
- Resolve conflicts manually
- Commit resolved changes

---

## Resources

- **Project Repository**: `/Users/vimalkrishnan/Workspace/revature/2353/review/p2/repos/p3-revworkforce`
- **Team Roles Document**: `/Users/vimalkrishnan/Workspace/revature/2353/review/p2/repos/REVWORKFORCE_TEAM_ROLES.md`
- **Spring Boot Documentation**: https://spring.io/projects/spring-boot
- **Spring Cloud Documentation**: https://spring.io/projects/spring-cloud
- **Eureka Documentation**: https://spring.io/projects/spring-cloud-netflix

---

**Document Version**: 1.0
**Last Updated**: 2026-03-05
**Generated with Claude Code**
