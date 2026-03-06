# Admin Service

**Owner:** Krishna Babu
**Port:** 8085
**Description:** Admin, Master Data and Notifications Service for RevWorkForce

## Overview

The Admin Service is a microservice responsible for managing administrative functions including:
- Master data management (Departments, Designations)
- Holiday management
- Company announcements
- Internal notifications

## Technology Stack

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Cloud Netflix Eureka Client
- Spring Cloud OpenFeign
- Resilience4j (Circuit Breaker)
- H2 Database (in-memory)
- Jakarta Validation

## Architecture

### Package Structure

```
com.revworkforce.admin
├── entity/              # JPA entities
│   ├── Department
│   ├── Designation
│   ├── Holiday
│   ├── Announcement
│   └── AdminNotification
├── dto/                 # Data Transfer Objects
│   ├── Request DTOs (with validation)
│   └── Response DTOs
├── repository/          # JPA repositories
├── service/             # Business logic
├── controller/          # REST endpoints
│   ├── AdminController          # Admin-only endpoints
│   ├── NotificationController   # Notification management
│   └── InternalController       # Internal service endpoints
├── client/              # Feign clients
│   └── EmployeeServiceClient
├── config/              # Configuration classes
│   ├── SecurityConfig
│   ├── FeignConfig
│   └── DataLoader
└── exception/           # Exception handling
```

## Entities

### Department
- **id** (Long): Primary key
- **name** (String): Unique department name

### Designation
- **id** (Long): Primary key
- **name** (String): Unique designation name

### Holiday
- **id** (Long): Primary key
- **name** (String): Holiday name
- **date** (LocalDate): Holiday date

### Announcement
- **id** (Long): Primary key
- **title** (String): Announcement title
- **content** (String): Announcement content (max 2000 chars)
- **createdAt** (LocalDateTime): Creation timestamp

### AdminNotification
- **id** (Long): Primary key
- **userId** (Long): Recipient user ID
- **message** (String): Notification message (max 1000 chars)
- **readFlag** (boolean): Read status
- **createdAt** (LocalDateTime): Creation timestamp

## API Endpoints

### Admin Endpoints (Require ADMIN role)

#### Departments
- `GET /api/admin/departments` - Get all departments
- `POST /api/admin/departments` - Create department
- `DELETE /api/admin/departments/{id}` - Delete department

#### Designations
- `GET /api/admin/designations` - Get all designations
- `POST /api/admin/designations` - Create designation
- `DELETE /api/admin/designations/{id}` - Delete designation

#### Holidays
- `GET /api/admin/holidays` - Get all holidays
- `GET /api/admin/holidays/year/{year}` - Get holidays by year
- `POST /api/admin/holidays` - Create holiday
- `DELETE /api/admin/holidays/{id}` - Delete holiday

#### Announcements
- `GET /api/admin/announcements` - Get all announcements
- `POST /api/admin/announcements` - Create announcement
- `PUT /api/admin/announcements/{id}` - Update announcement
- `DELETE /api/admin/announcements/{id}` - Delete announcement

### Notification Endpoints

- `GET /api/admin/notifications` - Get current user's notifications
- `POST /api/admin/notifications/send` - Send notification
- `POST /api/admin/notifications/broadcast` - Broadcast to all users (ADMIN only)
- `PATCH /api/admin/notifications/{id}/read` - Mark as read
- `GET /api/admin/notifications/unread/count` - Get unread count

### Internal Endpoints (For other services)

#### Departments
- `GET /api/internal/departments` - Get all departments
- `GET /api/internal/departments/{id}` - Get department by ID

#### Designations
- `GET /api/internal/designations` - Get all designations
- `GET /api/internal/designations/{id}` - Get designation by ID

#### Holidays
- `GET /api/internal/holidays` - Get all holidays
- `GET /api/internal/holidays/year/{year}` - Get holidays by year

## Security

Security is handled at the API Gateway level. User information is extracted from headers:
- **X-User-Id**: Current user's ID
- **X-User-Role**: Current user's role (ADMIN, EMPLOYEE, etc.)
- **X-User-Email**: Current user's email

All `/api/admin/**` endpoints require ADMIN role verification.

## Initial Data

The service automatically loads initial data on startup:

**Departments:**
- IT
- HR
- Finance

**Designations:**
- Software Engineer
- Team Lead
- HR Executive
- Administrator

## Feign Clients

### EmployeeServiceClient
Communicates with employee-service to:
- Fetch all employees for broadcast notifications
- Protected with Circuit Breaker pattern

## Circuit Breaker Configuration

Resilience4j circuit breaker is configured for `employee-service`:
- Sliding window size: 10
- Failure rate threshold: 50%
- Wait duration in open state: 5 seconds
- Automatic transition to half-open state enabled

## Running the Service

### Prerequisites
- Java 17
- Maven
- Eureka Server running on port 8761

### Local Development

```bash
# Build the service
mvn clean package

# Run the service
mvn spring-boot:run
```

The service will start on port 8085 and register with Eureka.

### Docker

```bash
# Build Docker image
docker build -t admin-service:latest .

# Run container
docker run -p 8085:8085 admin-service:latest
```

## Database

H2 in-memory database is used for development:
- Console: http://localhost:8085/h2-console
- JDBC URL: jdbc:h2:mem:admindb
- Username: sa
- Password: (empty)

## Health Check

- Health endpoint: http://localhost:8085/actuator/health
- Info endpoint: http://localhost:8085/actuator/info
- Metrics endpoint: http://localhost:8085/actuator/metrics

## Design Patterns

- **Builder Pattern**: Manual builder implementation for all DTOs and entities (no Lombok)
- **Repository Pattern**: Spring Data JPA repositories
- **Service Layer Pattern**: Business logic separation
- **Circuit Breaker Pattern**: Resilience4j for external service calls
- **DTO Pattern**: Request/Response DTOs with validation

## Error Handling

Global exception handler provides consistent error responses:
- `ResourceNotFoundException` → 404 NOT FOUND
- `DuplicateResourceException` → 409 CONFLICT
- `UnauthorizedException` → 403 FORBIDDEN
- `MethodArgumentNotValidException` → 400 BAD REQUEST
- Generic exceptions → 500 INTERNAL SERVER ERROR

## Validation

Jakarta Validation annotations are used:
- `@NotBlank`: For required string fields
- `@NotNull`: For required non-string fields
- `@Size`: For length constraints

## Key Features

1. **Master Data Management**: Centralized management of departments and designations
2. **Holiday Calendar**: Year-based holiday management
3. **Announcements**: Company-wide announcements with ordering
4. **Notifications**: User-specific and broadcast notifications
5. **Circuit Breaker**: Resilient communication with employee service
6. **Role-Based Access**: Admin role verification for sensitive operations
7. **Audit Timestamps**: Automatic timestamp generation for announcements and notifications

## Service Owner

**Krishna Babu** is responsible for maintaining and developing this service.
