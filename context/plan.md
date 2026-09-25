## Plan: Keycloak-Style Centralized IAM, SSO & Reusable Platform

Architect and evolve the `auth-parent` repository into an **Enterprise Centralized User Management, Identity & Access Management (IAM), and Single Sign-On (SSO) Platform** (Keycloak/Auth0 style) with Java 21 and Spring Boot 3.5.x.

```
                       ┌──────────────────────────────────────────────┐
                       │          User Management Platform            │
                       │          (Auth Server / IdP / SSO)           │
                       │                                              │
                       │   - Authentication & SSO    - User & Roles   │
                       │   - Email Service (SMTP)    - SMS Service    │
                       │   - JWT / JWKS Token Issuer - Audit Engine   │
                       └──────────────────────┬───────────────────────┘
                                              │
                                              │ Owns exclusively
                                              ▼
                                       ┌──────────────┐
                                       │   auth-db    │
                                       └──────────────┘
                                              │
                       Token Issuance / JWKS  │  REST / Client SDK
                   ┌──────────────────────────┼──────────────────────────┐
                   │                          │                          │
                   ▼                          ▼                          ▼
      ┌─────────────────────────┐┌─────────────────────────┐┌─────────────────────────┐
      │           LMS           ││           ERP           ││           HR            │
      │   (Resource Server)     ││   (Resource Server)     ││   (Resource Server)     │
      │                         ││                         ││                         │
      │ - Uses:                 ││ - Uses:                 ││ - Uses:                 │
      │   auth-client-starter   ││   auth-client-starter   ││   auth-client-starter   │
      │ - Verifies JWT locally  ││ - Verifies JWT locally  ││ - Verifies JWT locally  │
      │ - ZERO auth-db access   ││ - ZERO auth-db access   ││ - ZERO auth-db access   │
      └────────────┬────────────┘└────────────┬────────────┘└────────────┬────────────┘
                   │                          │                          │
                   ▼                          ▼                          ▼
            ┌──────────────┐           ┌──────────────┐           ┌──────────────┐
            │    lms-db    │           │    erp-db    │           │    hr-db     │
            └──────────────┘           └──────────────┘           └──────────────┘
```

---

### Core Architectural Invariants

1. **Database-Per-Service Isolation**:
   - `auth-db` belongs exclusively to the User Management Server.
   - Consumer microservices (LMS, ERP, HR) connect **ONLY** to their respective databases (`lms-db`, `erp-db`, `hr-db`).
   - Consumer microservices must **NEVER** connect to `auth-db`. No shared database anti-pattern.
2. **Stateless SSO Token Validation**:
   - The User Management Server signs JWT access tokens and handles refresh tokens.
   - Downstream services act as stateless OAuth2 / JWT Resource Servers using `auth-client-starter` to validate JWT signatures locally with zero database overhead.
3. **Centralized Common Platform Services**:
   - Email Service (SMTP / templates) and SMS Service are hosted centrally on the User Management server.
   - Downstream services trigger emails and SMS via REST API (`/api/v1/communications/email`, `/api/v1/communications/sms`) or through `auth-client-starter`'s `UserManagementClient` SDK.
4. **Tenant Isolation**:
   - Tenant context propagates via JWT token claims or `X-Tenant-Id` header, managed by `TenantFilter` / `TenantContext`.

---

### Module Hierarchy

| Module | Scope & Purpose |
| :--- | :--- |
| **`auth-common`** | Shared domain enums (`AuditAction`, `Gender`, `UserStatustus`), communication DTOs (`EmailRequest`, `SmsRequest`, `CommunicationResponse`). |
| **`auth-core`** | Server-side domain entities (`User`, `Role`, `Tenant`, `BaseEntity`), repositories, services (`UserService`, `EmailService`, `SmsService`, etc.). |
| **`auth-jwt`** | Server-side token signing (`JwtTokenProvider`) and refresh token persistence (`RefreshTokenService`). |
| **`auth-security`** | Spring Security 6 server configuration, password policies (`BCryptPasswordService`), authentication providers. |
| **`auth-service`** | Server REST controllers (`AuthController`, `UserController`, `CommunicationController`, `PasswordController`). |
| **`auth-autoconfigure`** | Auto-configuration for the standalone Auth Server. |
| **`auth-starter`** | Starter bundle for the Auth Server. |
| **`auth-client-starter`** | **Lightweight Client Starter** for consumer services (LMS, ERP, HR) — provides stateless JWT Resource Server security, `TenantFilter`, and `UserManagementClient` SDK. **Zero `auth-db` connection!** |
| **`auth-demo`** | Reference standalone User Management Server (port 8080). |
| **`lms`** | Consumer microservice (port 8089) using `auth-client-starter` and connecting solely to `lms_db`. |

---

### Implementation Progress

- [x] **Agent Rules & Foundation**: Configured `GEMINI.md` and 7 modular rules in `.agents/rules/`.
- [x] **Lightweight Client Starter**: Built `auth-client-starter` with `JwtValidator`, `JwtAuthenticationFilter`, `TenantFilter`, `ResourceServerSecurityConfiguration`, and `UserManagementClient` SDK.
- [x] **LMS Decoupling**: Replaced `auth-starter` in LMS with `auth-client-starter`, removed `auth-db` connections from `application.yml`, and added sample `Course` entity and `CourseController`.
- [x] **Centralized Communication APIs**: Added `EmailService`, `SmsService`, and `CommunicationController` (`/api/v1/communications/email`, `/api/v1/communications/sms`).
- [x] **Full Reactor Compilation**: Verified that all 11 modules compile cleanly via `.\mvnw.cmd clean compile`.