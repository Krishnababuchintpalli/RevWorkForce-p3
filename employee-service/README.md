# Employee Service

## Overview
Employee Service manages employee information and directory features for RevWorkForce. This service is owned by **Vamsi** and runs on **port 8082**.

## Features
- Employee profile management
- Directory search functionality
- Team management (manager-employee relationships)
- Employee activation/deactivation
- Department and designation integration

## Technology Stack
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Security
- Spring Cloud Netflix Eureka Client
- Spring Cloud OpenFeign
- Resilience4j Circuit Breaker
- H2 Database
- Jakarta Validation

## Port
8082

## Endpoints

### Employee Management
- `GET /api/employees/profile` - Get current employee profile
- `PUT /api/employees/profile` - Update profile (phone, address, emergencyContact)
- `GET /api/employees/team` - Get manager's direct reports
- `GET /api/employees` - Get all employees (ADMIN only)
- `POST /api/employees` - Create employee (ADMIN only)
- `GET /api/employees/{id}` - Get employee by ID
- `PATCH /api/employees/{id}/status` - Activate/deactivate employee (ADMIN only)
- `PATCH /api/employees/{id}/manager` - Assign manager (ADMIN only)

### Directory
- `GET /api/employees/search?q={query}` - Search employees by name or email
- `GET /api/employees/department/{departmentId}` - Get employees by department

### Internal APIs
- `GET /api/internal/employees/{id}` - Get employee summary for other services
- `POST /api/internal/employees/batch` - Get multiple employees by IDs
- `GET /api/internal/employees/user/{userId}` - Get employee by userId

## Security
- Authenticates via headers set by API Gateway
- `X-User-Id`: Current user's ID
- `X-User-Role`: Current user's role (ADMIN/EMPLOYEE)
- Only ADMIN role can create employees, change status, and assign managers

## Database
Uses H2 in-memory database for development. H2 Console available at: `http://localhost:8082/h2-console`
- URL: `jdbc:h2:mem:employeedb`
- Username: `sa`
- Password: `password`

## Feign Clients
- **AuthServiceClient**: Communicates with auth-service
- **AdminServiceClient**: Communicates with admin-service for department/designation info

## Circuit Breaker
Resilience4j circuit breaker configured for admin-service calls with fallback methods.

## Build and Run

### Prerequisites
- Java 17
- Maven

### Build
```bash
mvn clean package
```

### Run
```bash
mvn spring-boot:run
```

### Docker
```bash
docker build -t employee-service .
docker run -p 8082:8082 employee-service
```

## Service Registration
Registers with Eureka Discovery Server as `employee-service`

## Owner
Vamsi
