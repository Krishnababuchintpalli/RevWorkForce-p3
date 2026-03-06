# RevWorkForce Microservices

An HR Management platform built with Spring Boot microservices architecture.

## Architecture

```
                    ┌─────────────────┐
                    │   API Gateway   │
                    │    (8080)       │
                    └────────┬────────┘
                             │
         ┌───────────────────┼───────────────────┐
         │                   │                   │
         ▼                   ▼                   ▼
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│Auth Service │     │Employee Svc │     │Admin Service│
│   (8081)    │     │   (8082)    │     │   (8085)    │
│  [Ganesh]   │     │   [Vamsi]   │     │[Krishna Babu]│
└─────────────┘     └─────────────┘     └─────────────┘
         │                   │                   │
         │    ┌──────────────┴──────────────┐    │
         │    │                             │    │
         ▼    ▼                             ▼    ▼
┌─────────────────┐                 ┌─────────────────┐
│  Leave Service  │                 │Performance Svc  │
│    (8083)       │                 │    (8084)       │
│  [Sai Krishna]  │                 │ [Saideep Reddy] │
└─────────────────┘                 └─────────────────┘
```

## Services

| Service | Port | Description | Owner |
|---------|------|-------------|-------|
| Config Server | 8888 | Centralized configuration | Shared |
| Discovery Server | 8761 | Eureka service registry | Shared |
| API Gateway | 8080 | Request routing & JWT validation | Shared |
| Auth Service | 8081 | Authentication & JWT tokens | Ganesh |
| Employee Service | 8082 | Employee profiles & directory | Vamsi |
| Leave Service | 8083 | Leave management & notifications | Sai Krishna |
| Performance Service | 8084 | Performance reviews & goals | Saideep Reddy |
| Admin Service | 8085 | Master data & announcements | Krishna Babu |

## Prerequisites

- Java 17+
- Maven 3.8+
- Docker & Docker Compose (optional)

## Running Locally

### 1. Build all services
```bash
mvn clean package -DskipTests
```

### 2. Start services in order
```bash
# Terminal 1: Discovery Server
cd discovery-server && mvn spring-boot:run

# Terminal 2: API Gateway
cd api-gateway && mvn spring-boot:run

# Terminal 3-7: Business services (can start in parallel)
cd auth-service && mvn spring-boot:run
cd employee-service && mvn spring-boot:run
cd leave-service && mvn spring-boot:run
cd performance-service && mvn spring-boot:run
cd admin-service && mvn spring-boot:run
```

### Using Docker Compose
```bash
mvn clean package -DskipTests
docker-compose up --build
```

## API Endpoints

All requests go through the API Gateway at `http://localhost:8080`

### Auth Service (Ganesh)
- `POST /api/auth/login` - Login and get JWT
- `POST /api/auth/register` - Register new user (admin)
- `POST /api/auth/refresh` - Refresh JWT token
- `POST /api/auth/logout` - Logout
- `POST /api/auth/reset-password` - Reset password
- `GET /api/auth/validate` - Validate JWT token

### Employee Service (Vamsi)
- `GET /api/employees/profile` - Get current employee profile
- `PUT /api/employees/profile` - Update profile
- `GET /api/employees/search` - Search employees
- `GET /api/employees/team` - Get manager's team
- `GET /api/employees` - Get all employees (admin)
- `POST /api/employees` - Create employee (admin)
- `PATCH /api/employees/{id}/status` - Activate/deactivate
- `PATCH /api/employees/{id}/manager/{managerId}` - Assign manager

### Leave Service (Sai Krishna)
- `POST /api/leaves` - Apply for leave
- `GET /api/leaves/my` - Get user's leaves
- `GET /api/leaves/summary` - Get leave balance
- `GET /api/leaves/team` - Get team's leaves
- `PATCH /api/leaves/{id}/cancel` - Cancel leave
- `PATCH /api/leaves/{id}/approve` - Approve leave
- `PATCH /api/leaves/{id}/reject` - Reject leave

### Performance Service (Saideep Reddy)
- `POST /api/performance` - Create performance review
- `GET /api/performance/my` - Get user's reviews
- `GET /api/performance/team` - Get team reviews
- `PATCH /api/performance/{id}/feedback` - Provide feedback
- `POST /api/goals` - Create goal
- `GET /api/goals/my` - Get user's goals
- `GET /api/goals/team` - Get team goals
- `PATCH /api/goals/{id}/status` - Update goal status

### Admin Service (Krishna Babu)
- `GET/POST/DELETE /api/admin/departments` - Department CRUD
- `GET/POST/DELETE /api/admin/designations` - Designation CRUD
- `GET/POST/DELETE /api/admin/holidays` - Holiday CRUD
- `GET/POST/PUT/DELETE /api/admin/announcements` - Announcement CRUD
- `GET /api/admin/notifications` - Get notifications
- `POST /api/admin/notifications/send` - Send notification

## Technology Stack

- Spring Boot 3.2
- Spring Cloud (Eureka, Gateway)
- Spring Security + JWT
- Spring Data JPA
- H2 Database (dev)
- OpenFeign
- Resilience4j
- Docker
