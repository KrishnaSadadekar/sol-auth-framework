# REST API Design & Contract Standards

## Versioning & URI Structure

1. **API Prefix**:
   - All REST API endpoints must be explicitly versioned with the `/api/v1` prefix.
   - Core API endpoints:
     - `/api/v1/auth/login` (POST): Authenticate with username/password and receive tokens.
     - `/api/v1/auth/register` (POST): Register a new user account.
     - `/api/v1/auth/refresh` (POST): Exchange a valid refresh token for a new access/refresh pair.
     - `/api/v1/auth/logout` (POST): Revoke active refresh tokens.
     - `/api/v1/auth/me` (GET): Retrieve summary of currently authenticated user.
     - `/api/v1/users` (GET, POST): User management endpoints.
     - `/api/v1/passwords/*` (POST): Password reset and change endpoints.
2. **HTTP Verb Semantics**:
   - `POST`: Creation of resources or state transitions (login, register, refresh). Returns `201 Created` or `200 OK`.
   - `GET`: Safe, idempotent read operations. Returns `200 OK`.
   - `PUT` / `PATCH`: Full or partial resource updates. Returns `200 OK`.
   - `DELETE`: Resource removal or soft-deletion. Returns `204 No Content`.

---

## Strict DTO & Validation Conventions

1. **Entity Encapsulation**:
   - **Never return or accept JPA entities in REST controllers**. Exposing entities causes data leaks (e.g. password hashes), lazy loading exceptions outside transaction boundaries, and serialization circularities.
   - Always map entities to dedicated DTOs in `sol.auth.service.dto` (e.g., `UserSummaryResponse`, `AuthResponse`).
2. **Input Validation**:
   - Annotate controller classes with `@Validated` and request body parameters with `@Valid`.
   - Use Jakarta validation annotations on DTOs:
     ```java
     public class RegisterRequest {
         @NotBlank(message = "Username is required")
         @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
         private String username;

         @NotBlank(message = "Email is required")
         @Email(message = "Valid email is required")
         private String email;

         @NotBlank(message = "Password is required")
         @Size(min = 8, message = "Password must be at least 8 characters")
         private String password;
     }
     ```

---

## Standardized Error Envelope

All API errors must be intercepted by `GlobalExceptionHandler` and return a consistent JSON payload:

```json
{
  "code": "AUTH_INVALID_CREDENTIALS",
  "message": "Invalid username or password",
  "timestamp": "2026-09-25T11:30:00Z"
}
```

### Standard Error Codes & Status Mappings

| Exception | HTTP Status | Error Code |
| :--- | :--- | :--- |
| `TenantResolutionException` | 400 Bad Request | `TENANT_NOT_FOUND` |
| `MethodArgumentNotValidException` | 400 Bad Request | `VALIDATION_ERROR` |
| `IllegalArgumentException` | 400 Bad Request | `INVALID_REQUEST` |
| `InvalidCredentialsException` | 401 Unauthorized | `AUTH_INVALID_CREDENTIALS` |
| `UserDisabledException` | 403 Forbidden | `AUTH_USER_DISABLED` |
| `RoleNotFoundException` / `UserNotFoundException` | 404 Not Found | `RESOURCE_NOT_FOUND` |
| `UserAlreadyExistsException` | 409 Conflict | `AUTH_USER_EXISTS` |
| `RoleAlreadyExistsException` | 409 Conflict | `ROLE_ALREADY_EXISTS` |
| `AccountLockedException` | 423 Locked | `AUTH_ACCOUNT_LOCKED` |
| Generic unhandled `Exception` | 500 Internal Server Error | `INTERNAL_ERROR` |

---

## Header Conventions

1. **`X-Tenant-Id`**:
   - Required on tenant-scoped endpoints. Handled by `TenantFilter`.
2. **`X-Correlation-Id`**:
   - Optional incoming header. Handled by `CorrelationIdFilter`. If omitted, a UUID is generated and echoed back in the response header for distributed tracing.
3. **`Authorization`**:
   - Bearer token format: `Bearer <jwt-access-token>`.
