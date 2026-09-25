# REST API Design & Service Contracts

## API Layout & Versioning

All endpoints are versioned with the `/api/v1` prefix. The platform exposes three main categories of APIs:

### 1. Authentication & SSO Endpoints (`/api/v1/auth`)
- `POST /api/v1/auth/login`: Authenticate credentials, return access token + refresh token.
- `POST /api/v1/auth/register`: Register new user (initiates welcome/verification email).
- `POST /api/v1/auth/refresh`: Rotate refresh token and issue new token pair.
- `POST /api/v1/auth/logout`: Invalidate refresh tokens.
- `GET /api/v1/auth/me`: Return authenticated user profile and roles.
- `GET /.well-known/jwks.json`: Public JSON Web Key Set for client Resource Servers to verify JWT signatures.

### 2. User & Tenant Management (`/api/v1/users`, `/api/v1/tenants`)
- `GET /api/v1/users`: List users (filtered by tenant).
- `GET /api/v1/users/{id}`: Get user details by ID.
- `PUT /api/v1/users/{id}/roles`: Assign/update user roles.
- `POST /api/v1/tenants`: Create a new tenant.

### 3. Centralized Communication Services (`/api/v1/communications`)
Downstream microservices (LMS, ERP, HR) invoke these endpoints (or use `auth-client-starter` SDK) instead of maintaining their own SMTP or SMS providers:
- `POST /api/v1/communications/email`: Send an email (supports templates, subject, recipient, attachments).
- `POST /api/v1/communications/sms`: Send an SMS notification / OTP.

---

## Contract Standards & DTO Encapsulation

1. **Zero Entity Exposure**:
   - Controller endpoints must never expose JPA entities (`User`, `Role`, `Tenant`).
   - Use dedicated DTOs (e.g., `LoginRequest`, `RegisterRequest`, `AuthResponse`, `UserSummaryResponse`, `EmailRequest`).
2. **Standard Error Envelope**:
   - All errors are formatted by `GlobalExceptionHandler`:
     ```json
     {
       "code": "AUTH_INVALID_CREDENTIALS",
       "message": "Invalid username or password",
       "timestamp": "2026-09-25T11:45:00Z"
     }
     ```
3. **HTTP Status Code Conventions**:
   - `200 OK`: Successful read or token refresh.
   - `201 Created`: Successful creation (register, tenant create).
   - `204 No Content`: Successful logout or deletion.
   - `400 Bad Request`: Validation failure or missing parameters.
   - `401 Unauthorized`: Bad credentials or expired/invalid token.
   - `403 Forbidden`: Insufficient role or disabled account.
   - `404 Not Found`: Target user/tenant/resource does not exist.
   - `409 Conflict`: Username or email already registered.
   - `423 Locked`: Account locked due to failed attempts.
   - `500 Internal Server Error`: Unhandled server exceptions.

---

## Headers & Tracing

- `Authorization`: `Bearer <jwt-token>`
- `X-Tenant-Id`: Tenant identifier for tenant-scoped operations.
- `X-Correlation-Id`: Distributed trace ID propagated across services by `CorrelationIdFilter`.
