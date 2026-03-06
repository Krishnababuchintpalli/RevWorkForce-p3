# Leave Service

**Owner:** Sai Krishna
**Port:** 8083
**Description:** Leave & Leave-Notifications Service for RevWorkForce

## Overview

The Leave Service manages employee leave requests and notifications within the RevWorkForce HR management platform. It handles leave application, approval/rejection workflows, leave balance tracking, and notifications for leave-related events.

## Features

- **Leave Management**
  - Apply for leave with various types (Sick, Personal, Vacation, etc.)
  - View personal leave history
  - Cancel pending leave requests
  - Track leave balance (24 days allocated per year)
  - Calculate leave days automatically

- **Leave Approval Workflow**
  - Manager approval/rejection with comments
  - Admin override capabilities
  - Mandatory comments for rejections
  - Status tracking (Pending, Approved, Rejected, Canceled)

- **Team Management**
  - Managers can view direct reports' leaves
  - Admins can view all leaves
  - Team leave calendar functionality

- **Notifications**
  - Automatic notifications for leave submissions
  - Approval/rejection notifications
  - Unread notification tracking
  - Mark notifications as read

## Technology Stack

- **Framework:** Spring Boot 3.2.0
- **Java Version:** 17
- **Database:** H2 (in-memory)
- **Service Discovery:** Eureka Client
- **Inter-Service Communication:** OpenFeign
- **Resilience:** Resilience4j Circuit Breaker
- **Validation:** Jakarta Validation

## Package Structure

```
com.revworkforce.leave
├── client/                     # Feign clients for inter-service communication
│   └── EmployeeServiceClient.java
├── config/                     # Configuration classes
│   ├── FeignConfig.java
│   ├── LeaveConfig.java
│   └── SecurityConfig.java
├── controller/                 # REST controllers
│   ├── LeaveController.java
│   └── LeaveNotificationController.java
├── dto/                        # Data Transfer Objects
│   ├── EmployeeDto.java
│   ├── LeaveApplyRequest.java
│   ├── LeaveDecisionRequest.java
│   ├── LeaveNotificationResponse.java
│   ├── LeaveResponse.java
│   ├── LeaveSummaryResponse.java
│   └── TeamLeaveResponse.java
├── entity/                     # JPA entities
│   ├── LeaveNotification.java
│   └── LeaveRequest.java
├── enums/                      # Enumerations
│   ├── LeaveStatus.java
│   └── LeaveType.java
├── exception/                  # Custom exceptions and handlers
│   ├── GlobalExceptionHandler.java
│   ├── LeaveException.java
│   └── ResourceNotFoundException.java
├── repository/                 # JPA repositories
│   ├── LeaveNotificationRepository.java
│   └── LeaveRequestRepository.java
├── service/                    # Business logic
│   ├── LeaveNotificationService.java
│   └── LeaveService.java
└── LeaveServiceApplication.java
```

## API Endpoints

### Leave Management

#### Apply for Leave
```http
POST /api/leaves
Content-Type: application/json

{
  "leaveType": "VACATION",
  "startDate": "2024-03-15",
  "endDate": "2024-03-20",
  "reason": "Family vacation"
}
```

#### Get My Leaves
```http
GET /api/leaves/my
```

#### Get Leave Summary
```http
GET /api/leaves/summary

Response:
{
  "totalAllocated": 24,
  "used": 10,
  "pending": 3,
  "remaining": 11
}
```

#### Get Team Leaves (Manager/Admin)
```http
GET /api/leaves/team
```

#### Cancel Leave
```http
PATCH /api/leaves/{id}/cancel
```

#### Approve Leave (Manager/Admin)
```http
PATCH /api/leaves/{id}/approve
Content-Type: application/json

{
  "comment": "Approved for vacation"
}
```

#### Reject Leave (Manager/Admin)
```http
PATCH /api/leaves/{id}/reject
Content-Type: application/json

{
  "comment": "Required during critical project phase"
}
```

### Notifications

#### Get Notifications
```http
GET /api/leaves/notifications
```

#### Mark Notification as Read
```http
PATCH /api/leaves/notifications/{id}/read
```

#### Get Unread Count
```http
GET /api/leaves/notifications/unread/count

Response:
{
  "count": 5
}
```

## Database Schema

### LeaveRequest Table
- `id` (Long, Primary Key)
- `employee_id` (Long, Not Null)
- `leave_type` (Enum: SICK, PERSONAL, VACATION, MATERNITY, PATERNITY, BEREAVEMENT, UNPAID)
- `start_date` (LocalDate, Not Null)
- `end_date` (LocalDate, Not Null)
- `reason` (String, max 1000 chars)
- `status` (Enum: PENDING, APPROVED, REJECTED, CANCELED)
- `manager_comment` (String, max 1000 chars)
- `created_at` (LocalDateTime)
- `updated_at` (LocalDateTime)

### LeaveNotification Table
- `id` (Long, Primary Key)
- `user_id` (Long, Not Null)
- `message` (String, max 1000 chars)
- `read_flag` (Boolean, default false)
- `created_at` (LocalDateTime)

## Business Rules

1. **Leave Application**
   - End date must be greater than or equal to start date
   - Employee must have sufficient leave balance
   - Leave balance = 24 - approved leaves - pending leaves

2. **Leave Cancellation**
   - Only employees can cancel their own leaves
   - Only PENDING leaves can be canceled

3. **Leave Approval**
   - Only the employee's manager or admin can approve/reject
   - Only PENDING leaves can be approved/rejected
   - Rejection requires a mandatory comment

4. **Leave Calculation**
   - Leave days calculated as: end_date - start_date + 1
   - Only leaves in the current year count toward balance

## Configuration

### application.yml
```yaml
server:
  port: 8083

spring:
  application:
    name: leave-service
  datasource:
    url: jdbc:h2:mem:leavedb

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

leave:
  total-allocated: 24
```

## Inter-Service Communication

### Employee Service Integration
The Leave Service communicates with the Employee Service using Feign Client:
- Get employee details by ID
- Get employee details by user ID
- Batch fetch employees
- Retrieve manager information

## Security

- User authentication handled by API Gateway
- User ID and Role extracted from request headers (X-User-Id, X-User-Role)
- Role-based access control for manager/admin operations
- Request context forwarded through Feign interceptor

## Error Handling

- **ResourceNotFoundException**: 404 - Resource not found
- **LeaveException**: 400 - Business rule violations
- **ValidationException**: 400 - Invalid request data
- **InternalServerError**: 500 - Unexpected errors

## Running the Service

### Prerequisites
- Java 17
- Maven 3.6+
- Eureka Discovery Server running on port 8761
- Employee Service running on its designated port

### Build
```bash
mvn clean package
```

### Run
```bash
java -jar target/leave-service-1.0.0.jar
```

### Docker
```bash
docker build -t leave-service .
docker run -p 8083:8083 leave-service
```

## H2 Console

Access the H2 console at: http://localhost:8083/h2-console
- JDBC URL: jdbc:h2:mem:leavedb
- Username: sa
- Password: (empty)

## Service Registration

The service registers with Eureka Discovery Server as: `leave-service`

## Health Check

```http
GET /actuator/health
```

## Circuit Breaker

Resilience4j circuit breaker configured for employee-service calls:
- Sliding window size: 10
- Failure rate threshold: 50%
- Wait duration in open state: 10 seconds
- Permitted calls in half-open state: 3

## Design Patterns

- **Builder Pattern**: Manual builder pattern for all DTOs and entities (no Lombok)
- **Repository Pattern**: JPA repositories for data access
- **Service Layer Pattern**: Business logic in service classes
- **DTO Pattern**: Separate DTOs for requests and responses
- **Exception Handling**: Centralized exception handling with @RestControllerAdvice

## Future Enhancements

- Leave policy configuration per department
- Half-day leave support
- Leave rollover functionality
- Calendar integration
- Email notifications
- Leave approval delegation
- Public holiday management
