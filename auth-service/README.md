# Auth Service - RevWorkForce

Authentication and Authorization microservice for RevWorkForce platform.

**Owner:** Ganesh
**Port:** 8081

## Features

- User registration and authentication
- JWT token generation and validation
- Refresh token management
- Password reset functionality
- Role-based access control (EMPLOYEE, MANAGER, ADMIN)
- Internal user validation for other microservices

## Tech Stack

- Java 17
- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- H2 Database
- JWT (jjwt 0.12.3)
- Netflix Eureka Client
- Maven

## API Endpoints

### Public Endpoints

- `POST /api/auth/login` - User login
- `POST /api/auth/register` - Register new user (admin only)
- `POST /api/auth/refresh` - Refresh JWT token
- `POST /api/auth/logout` - Logout user
- `POST /api/auth/reset-password` - Reset user password

### Internal Endpoints (for microservices)

- `GET /api/auth/validate` - Validate JWT token
- `GET /api/auth/user/{userId}` - Get user details by ID

## Default Credentials

- **Email:** admin@revworkforce.com
- **Password:** Admin@123

## Configuration

- **Port:** 8081
- **Database:** H2 in-memory
- **Eureka Server:** http://localhost:8761/eureka/
- **JWT Expiration:** 24 hours
- **Refresh Token Expiration:** 7 days

## Running the Service

### Using Maven

```bash
mvn spring-boot:run
```

### Using Docker

```bash
docker build -t auth-service .
docker run -p 8081:8081 auth-service
```

## H2 Console

Access H2 database console at: http://localhost:8081/h2-console

- **JDBC URL:** jdbc:h2:mem:authdb
- **Username:** sa
- **Password:** (leave empty)

## Roles

1. **EMPLOYEE (ID: 1)** - Standard employee access
2. **MANAGER (ID: 2)** - Manager level access
3. **ADMIN (ID: 3)** - Full administrative access
