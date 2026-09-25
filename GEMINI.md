# User Management & SSO Platform — Keycloak-Style Architecture

## System Vision & Architectural Paradigm

This project is an **Enterprise Centralized User Management, Identity & Access Management (IAM), and SSO Platform** (similar in role to Keycloak, Auth0, or Ory), built with **Java 21** and **Spring Boot 3.5.x**.

It provides centralized, reusable platform services to avoid reimplementing them across multiple business microservices:
1. **Centralized Authentication & Single Sign-On (SSO)**
2. **Tenant-Aware User, Role & Permission Management**
3. **Common Communication Services**: Email Service (SMTP, templates) & SMS Service
4. **Audit Logging & Security Enforcement**

```
                       ┌──────────────────────────────────────────────┐
                       │           User Management Platform           │
                       │           (Auth Server / IdP / SSO)          │
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

## The Golden Architectural Rules

### 1. Strict Database-per-Service (No Shared Database Anti-Pattern)
- **`auth-db` belongs exclusively to the User Management Server**.
- Consumer microservices (**LMS**, **ERP**, **HR**) connect **ONLY** to their own business databases (`lms-db`, `erp-db`, `hr-db`).
- **LMS must NEVER connect to `auth-db`**. It must have NO datasource, NO Flyway migrations, and NO JPA entities for auth tables.

### 2. Single Sign-On (SSO) & Stateless Token Verification
- The **User Management Server** acts as the Identity Provider (IdP):
  - Authenticates users against `auth-db`.
  - Issues digitally signed JWT access tokens (asymmetric RSA / JWKS or HMAC) and refresh tokens.
  - Exposes `/.well-known/jwks.json` or public key endpoint for token validation.
- Consumer microservices (**LMS**, **ERP**, **HR**) act as **OAuth2 / JWT Resource Servers**:
  - They validate incoming `Bearer <token>` statelessly using the Auth Server's public key.
  - Zero database hits to `auth-db` are needed to verify authentication or extract roles/tenant!

### 3. Client Starter vs. Server Components
- **`auth-client-starter` (for LMS, ERP, HR)**:
  - Lightweight Spring Boot starter included by consumer microservices.
  - Configures Spring Security as a **Resource Server** (validates JWT, extracts `AuthUserPrincipal`, `tenantId`, `roles`, `permissions`).
  - Provides `TenantFilter` / `TenantContext`.
  - Provides a Client SDK (`UserManagementClient`) to call Auth Server APIs (e.g. `sendEmail`, `sendSms`, `getUserProfile`).
  - **Contains ZERO JPA, ZERO DataSource, and ZERO entity mapping for auth tables.**
- **`auth-server` / `auth-service`**:
  - Standalone service running on its own port (e.g., 8080).
  - Contains all JPA entities (`User`, `Role`, `Tenant`, `BaseEntity`), repositories, business services, and database migrations for `auth-db`.

### 4. Common Services (Email, SMS, Audit)
- Common services are hosted centrally within the User Management platform.
- Client applications consume them via:
  - **REST API**: e.g., `POST /api/v1/communications/email`, `POST /api/v1/communications/sms`.
  - **Client SDK**: Provided in `auth-client-starter` for one-line invocations: `notificationClient.sendEmail(request)`.
  - **Event-Driven**: Async messaging via Kafka/RabbitMQ or Spring events.

---

## Module Breakdown

| Module | Role | Dependencies & Scope |
| :--- | :--- | :--- |
| **`auth-common`** | Shared Enums & Primitives | Enums (`AuditAction`, `Gender`, `UserStatustus`), common DTOs, and constants. Shared by both server and client. |
| **`auth-core`** | Core Server Domain | JPA entities (`User`, `Role`, `Tenant`, `BaseEntity`), Spring Data repositories, core services (`UserService`, `RoleService`, `LoginAttemptService`, `AccountLockService`). Used **ONLY** by the Auth Server. |
| **`auth-security`** | Security Foundations | Security 6 configurations, password policies (`BCryptPasswordService`), `AuthUserPrincipal`, authentication providers. |
| **`auth-jwt`** | Token Issuance & Verification | `JwtTokenProvider`, key management (RSA / HMAC), refresh token persistence and rotation. |
| **`auth-service`** | Server REST API & Controllers | Central REST controllers (`AuthController`, `UserController`, `NotificationController`), request/response DTOs, `GlobalExceptionHandler`. |
| **`auth-client-starter`** | Client Resource Server Starter | Lightweight starter for **LMS, ERP, HR**. Automatically configures JWT verification, `TenantFilter`, and API client SDK. **NO database connection to `auth-db`!** |
| **`auth-demo`** | Reference Auth Server | Standalone server demonstrating running the User Management / SSO service on `auth-db`. |
| **`lms`** | Consumer Microservice | Learning Management System connecting **ONLY** to `lms-db`, using `auth-client-starter` for SSO and security. |

---

## Build & Execution Commands

From the project root using Windows PowerShell (`mvnw.cmd`):

```powershell
# Compile all modules
.\mvnw.cmd clean compile

# Build reactor skipping tests
.\mvnw.cmd clean install -DskipTests

# Run the central User Management Server (Demo / Auth Server)
.\mvnw.cmd spring-boot:run -pl auth-demo

# Run the LMS microservice (runs on port 8089 with lms-db only)
.\mvnw.cmd spring-boot:run -pl lms
```

---

## Modular Rules Reference

- [architecture.md](file:///d:/MyWork/Project/Workspace/Usermanagement/auth-parent/.agents/rules/architecture.md): Keycloak-style SSO, microservices boundaries, database-per-service pattern, client starter vs server separation.
- [database.md](file:///d:/MyWork/Project/Workspace/Usermanagement/auth-parent/.agents/rules/database.md): Strict database-per-service isolation; client apps never touch `auth-db`.
- [security.md](file:///d:/MyWork/Project/Workspace/Usermanagement/auth-parent/.agents/rules/security.md): SSO token lifecycle, asymmetric JWT validation, Resource Server setup, and password policy.
- [api.md](file:///d:/MyWork/Project/Workspace/Usermanagement/auth-parent/.agents/rules/api.md): REST contracts for Auth, SSO, User Management, and Communication (Email/SMS).
- [java-spring.md](file:///d:/MyWork/Project/Workspace/Usermanagement/auth-parent/.agents/rules/java-spring.md): Java 21, Spring Boot 3.5.x, constructor injection, and event handling.
- [testing.md](file:///d:/MyWork/Project/Workspace/Usermanagement/auth-parent/.agents/rules/testing.md): Resource Server tests, token validation tests, and tenant isolation tests.
- [git.md](file:///d:/MyWork/Project/Workspace/Usermanagement/auth-parent/.agents/rules/git.md): Commit conventions, branch naming, and security hygiene.
