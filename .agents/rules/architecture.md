# Architecture & Modular Design Guidelines

## Module Hierarchy & Strict Dependency Rules

The `auth-parent` codebase is structured into cohesive, decoupled modules. The agent must strictly respect the unidirectional dependency chain:

```
auth-common (No dependencies on other auth modules)
    │
    ▼
auth-core (Depends on: auth-common)
    │
    ▼
auth-security (Depends on: auth-core, auth-common)
    │
    ▼
auth-jwt (Depends on: auth-core, auth-common)
    │
    ▼
auth-service (Depends on: auth-jwt, auth-security, auth-core, auth-common)
    │
    ▼
auth-autoconfigure (Depends on: auth-service, auth-jwt, auth-security, auth-core, auth-common)
    │
    ▼
auth-starter (Aggregates: auth-autoconfigure, auth-service, etc.)
    │
    ├────────────────────────┐
    ▼                        ▼
auth-demo                   lms (Client application consuming auth-starter)
```

### Module Boundaries & Invariants
1. **Never introduce upward dependencies**: E.g., `auth-core` must **never** import classes from `auth-security`, `auth-jwt`, or `auth-service`.
2. **Never create circular dependencies**: Modules must compile in clean reactor order.
3. **Pluggable Architecture**:
   - `auth-core` defines the contracts (interfaces such as `PasswordService`, `AuthenticationService`, `AuthorizationService`, `AuditLogService`, `AccountLockService`).
   - `auth-security` and `auth-jwt` provide specific technical implementations (`BCryptPasswordService`, `JwtTokenProviderImpl`, `RefreshTokenServiceImpl`).
   - `auth-autoconfigure` binds them conditionally.

---

## Multi-Tenancy Architecture

Multi-tenancy is a core first-class requirement implemented via the **shared database, shared schema with tenant column** pattern.

1. **`BaseEntity` Tenancy**:
   - Every domain entity in `auth-core` inherits from `BaseEntity`.
   - `tenantId` is mapped via `@Column(name = "tenant_id")`.
2. **Tenant Context Propagation**:
   - Requests provide the tenant identifier via the `X-Tenant-Id` HTTP header.
   - `TenantFilter` in `auth-service` parses the header and populates `TenantContext.setTenantId(Long tenantId)`.
   - In every filter or request interceptor, clearing the context is mandatory:
     ```java
     try {
         TenantContext.setTenantId(tenantId);
         chain.doFilter(request, response);
     } finally {
         TenantContext.clear();
     }
     ```
3. **Tenant Scoping in Repositories & Services**:
   - Queries must always filter by `tenantId` whenever operating on tenant-scoped data.
   - Uniqueness checks (e.g., username, email, role name) must be scoped by `tenantId` (e.g., `findByUsernameAndTenantId(...)`).
   - Prevent cross-tenant data leakage: users from Tenant A must never be able to view, modify, or authenticate against Tenant B credentials.

---

## Auto-Configuration & Starter Conventions

When writing or modifying auto-configuration beans in `auth-autoconfigure`:

1. **Conditional Registration**:
   - Use `@AutoConfiguration` as the primary configuration class annotation.
   - Always protect bean definitions with `@ConditionalOnMissingBean` to give consuming applications the ability to override default implementations.
   - Use `@ConditionalOnProperty` for opt-in or feature-flagged functionality (e.g., auditing, rate limiting, communication).
2. **Configuration Properties**:
   - Centralize properties under `@ConfigurationProperties(prefix = "auth")` or sub-namespaces (e.g., `auth.jwt`, `auth.security`, `auth.datasource`).
   - Provide safe development fallbacks and validate properties on startup.
3. **Dual-Datasource Isolation**:
   - Do not overwrite the consuming application's primary `DataSource` or `EntityManagerFactory`.
   - The framework configures dedicated `authDataSource`, `authEntityManagerFactory`, and `authTransactionManager` beans with explicit persistence unit names (`authPersistenceUnit`) and package scanning (`sol.auth.core.entity`, `sol.auth.core.repository`).

---

## Separation of Concerns

1. **Domain vs. Web**:
   - `auth-core` must not import `jakarta.servlet.*` or `org.springframework.web.*`. Web and HTTP concepts are isolated to `auth-service` and `auth-security`.
2. **Entity vs. DTO**:
   - Never use JPA entities as request bodies or response payloads in controllers.
   - All controller endpoints must consume and return dedicated DTOs (e.g., `LoginRequest`, `RegisterRequest`, `AuthResponse`, `UserSummaryResponse`).
3. **Event-Driven Decoupling**:
   - Use Spring `ApplicationEventPublisher` to emit domain events (`UserRegisteredEvent`, `UserLoggedInEvent`, `UserLockedEvent`, `UserPasswordChangedEvent`) from services.
   - Listeners handle cross-cutting concerns (e.g., sending emails, writing audit logs) asynchronously via `@Async` without coupling core auth workflows.
