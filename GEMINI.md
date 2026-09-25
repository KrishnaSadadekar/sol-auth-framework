# Auth Parent — Enterprise Reusable Authentication & User Management Platform

## Project Overview

`auth-parent` is an enterprise-grade, multi-tenant authentication, authorization, and user management platform built with **Java 21** and **Spring Boot 3.5.x**. It is engineered with a dual distribution model:
1. **Java Library / Spring Boot Starter (`auth-starter`)**: Other Spring Boot applications (such as `lms`) can include a single starter dependency to acquire complete auth, user management, and security capabilities with sensible defaults and auto-configuration.
2. **REST API Façade (`auth-service`)**: Microservices and cross-language clients (Python, Node.js, React/Vue frontends) interact with authentication, authorization, and user management through standardized, versioned REST endpoints (`/api/v1/auth`, `/api/v1/users`, `/api/v1/passwords`).

---

## Architecture & Modular Breakdown

The project follows a clean, decoupled multi-module Maven structure with strict dependency hierarchy:

```
auth-common
    │
    ▼
auth-core
    │
    ▼
auth-security
    │
    ▼
auth-jwt
    │
    ▼
auth-service
    │
    ├───────────────────────┬────────────────────────┐
    ▼                       ▼                        ▼
auth-autoconfigure     auth-demo                    lms
    │
    ▼
auth-starter
```

### Module Responsibilities

| Module | Purpose | Key Responsibilities & Components |
| :--- | :--- | :--- |
| **`auth-common`** | Shared Domain Enums & Primitives | Base enums (`AuditAction`, `Gender`, `UserStatustus`), common utility constants. Zero dependencies on higher layers. |
| **`auth-core`** | Core Domain & Business Logic | JPA entities (`BaseEntity`, `User`, `Role`, `Permission`, `Tenant`, `UserRole`, `RolePermission`, `LoginAttempt`, `PasswordHistory`, `RefreshToken`, `UserSession`), Spring Data repositories, core service interfaces & implementations (`UserService`, `RoleService`, `PermissionService`, `AuthenticationService`, `LoginAttemptService`, `AccountLockService`, `AuditLogService`, `RegistrationService`), domain events (`UserRegisteredEvent`, `UserLoggedInEvent`, `UserLockedEvent`), and `TenantContext`. |
| **`auth-security`** | Spring Security Integration | Spring Security 6 configurations, stateless `JwtAuthenticationFilter`, `AuthUserPrincipal`, `CustomAuthenticationProvider`, `AuthUserDetailsService`, password policy enforcement (`PasswordPolicyValidator`, `BCryptPasswordService`), and `SpringSecurityAuditorAware`. |
| **`auth-jwt`** | JWT & Refresh Token Lifecycle | `JwtTokenProvider` (HMAC/RSA token generation & claims parsing), `RefreshTokenService` (token rotation, persistence, and revocation), configuration properties (`JwtTokenProperties`). |
| **`auth-service`** | REST API Façade | REST controllers (`AuthController`, `UserController`, `PasswordController`), request/response DTOs (`LoginRequest`, `RegisterRequest`, `AuthResponse`, `UserSummaryResponse`), unified `GlobalExceptionHandler`, request filters (`TenantFilter`, `CorrelationIdFilter`). |
| **`auth-autoconfigure`** | Spring Boot Auto-Configuration | `@AutoConfiguration` classes (`AuthAutoConfiguration`, `AuthSecurityAutoConfiguration`), properties (`AuthPlatformProperties`), separate `authDataSource`, `authEntityManagerFactory`, and `authTransactionManager` beans for multi-datasource support. |
| **`auth-starter`** | Unified Dependency Entrypoint | Transitive dependency aggregator packaging `auth-autoconfigure`, `auth-service`, `auth-security`, `auth-jwt`, and `auth-core` for plug-and-play client integration. |
| **`auth-demo`** | Reference Implementation | Reference application showing standalone setup and configuration of the auth platform. |
| **`lms`** | Consumer Application | Real-world consumer application (Learning Management System) demonstrating dual-datasource isolation (`lms_db` for business data, `auth_db` for auth data). |

---

## Agent Development Principles

When developing code or making modifications in this repository, the agent MUST adhere to the following core guidelines:

### 1. Architectural Boundaries & Dependency Flow
- **Never create circular dependencies**: Dependencies flow strictly downward: `common` -> `core` -> `security` -> `jwt` -> `service`.
- **Entity & DTO Separation**: Never expose JPA entities (`User`, `Role`, `Tenant`, etc.) directly through REST controllers. Always map to/from dedicated DTOs in `auth-service/src/main/java/sol/auth/service/dto`.
- **Statelessness**: No HTTP sessions are to be created for authentication. The security model is strictly stateless via bearer JWT tokens.

### 2. Multi-Tenancy Architecture
- All core entities inherit from `BaseEntity` which contains `tenantId`.
- Every incoming HTTP request must resolve tenant context via `TenantFilter` (from the `X-Tenant-Id` header) and store it in `TenantContext`.
- Always clear `TenantContext` in a `finally` block or filter completion to prevent ThreadLocal memory leakage.
- Repository queries and authorization checks must be tenant-aware to guarantee strict tenant data isolation.

### 3. Dual-Datasource & Persistence Isolation
- In consumer applications like `lms`, business entities use the primary datasource, while auth entities live in the `authDataSource` configured in `AuthAutoConfiguration`.
- Ensure repository interfaces in `sol.auth.core.repository` use `authTransactionManager` and the `authEntityManagerFactory`.
- Schema migrations for the auth framework must remain decoupled from client application migrations.

### 4. Auto-Configuration & Extensibility
- Service beans exposed by the framework must use `@ConditionalOnMissingBean` where appropriate, allowing consuming applications to provide custom overrides (e.g. custom password hashing, custom user lookup, or custom audit sinks).
- All configuration keys must be registered with `@ConfigurationProperties` and documented under the `auth.*` prefix.

### 5. Error Handling & Contract Consistency
- All exceptions thrown across services must be translated by `GlobalExceptionHandler` into a unified `ErrorResponse` envelope containing: `code`, `message`, `timestamp`, and optional validation details.
- Avoid raw stack traces or leaked internal database exception messages to clients.

---

## Build & Run Commands

All commands should be executed from the project root using the provided Maven wrapper (`mvnw.cmd` on Windows PowerShell):

### Build and Compile
```powershell
# Clean and compile all modules
.\mvnw.cmd clean compile

# Full build skipping tests
.\mvnw.cmd clean install -DskipTests

# Build a specific module (e.g., auth-core)
.\mvnw.cmd clean install -pl auth-core -am
```

### Run Tests
```powershell
# Run all tests across the reactor
.\mvnw.cmd test

# Run tests for a specific module
.\mvnw.cmd test -pl auth-security

# Run a single test class
.\mvnw.cmd test -Dtest=SecurityConfigurationTest
```

### Run Consumer / Demo Applications
```powershell
# Run the LMS consumer application (port 8089)
.\mvnw.cmd spring-boot:run -pl lms

# Run the Auth Demo application
.\mvnw.cmd spring-boot:run -pl auth-demo
```

---

## Rules Directory Reference

Detailed, domain-specific rules are modularized under `.agents/rules/`:

- [architecture.md](file:///d:/MyWork/Project/Workspace/Usermanagement/auth-parent/.agents/rules/architecture.md): Module hierarchy, dependency direction, multi-tenancy, and auto-configuration design.
- [java-spring.md](file:///d:/MyWork/Project/Workspace/Usermanagement/auth-parent/.agents/rules/java-spring.md): Java 21 features, Spring Boot 3.5.x standards, Lombok practices, and Bean lifecycle.
- [security.md](file:///d:/MyWork/Project/Workspace/Usermanagement/auth-parent/.agents/rules/security.md): Spring Security 6, JWT lifecycle, password hashing, account lockout, and CORS/CSRF rules.
- [database.md](file:///d:/MyWork/Project/Workspace/Usermanagement/auth-parent/.agents/rules/database.md): JPA mapping, BaseEntity auditing, dual-datasource configuration, transactions, and Flyway.
- [api.md](file:///d:/MyWork/Project/Workspace/Usermanagement/auth-parent/.agents/rules/api.md): REST API design, `/api/v1` versioning, DTO mapping, error response envelope, and HTTP headers.
- [testing.md](file:///d:/MyWork/Project/Workspace/Usermanagement/auth-parent/.agents/rules/testing.md): Unit and integration testing standards, Mockito, slice tests, and tenant isolation tests.
- [git.md](file:///d:/MyWork/Project/Workspace/Usermanagement/auth-parent/.agents/rules/git.md): Commit conventions, branch strategy, PR quality gates, and security hygiene.
