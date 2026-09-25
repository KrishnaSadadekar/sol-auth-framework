# Database & Persistence Guidelines

## Strict Database-Per-Service Architecture

In accordance with microservices best practices and the Keycloak-style architecture:

```
┌─────────────────────────────────┐
│  User Management / Auth Server  │ ──────► auth-db (MySQL)
└─────────────────────────────────┘
┌─────────────────────────────────┐
│               LMS               │ ──────► lms-db (MySQL / Postgres)
└─────────────────────────────────┘
┌─────────────────────────────────┐
│               ERP               │ ──────► erp-db (MySQL / Postgres)
└─────────────────────────────────┘
┌─────────────────────────────────┐
│               HR                │ ──────► hr-db (MySQL / Postgres)
└─────────────────────────────────┘
```

### Absolute Isolation Rules
1. **No Shared Database**:
   - `auth-db` is strictly private to the User Management Server.
   - Client applications (**LMS**, **ERP**, **HR**) **MUST NEVER connect to `auth-db`**.
   - Client applications must **NOT** define an `authDataSource`, must not configure credentials for `auth-db`, and must not run Flyway migrations targeting `auth-db`.
2. **Single Datasource in Consumer Applications**:
   - LMS configures only its own business datasource via standard Spring Boot properties:
     ```yaml
     spring:
       datasource:
         url: jdbc:mysql://localhost:3306/lms_db
         username: lms_user
         password: lms_password
     ```
   - No dual-datasource complexity is allowed in client applications.

---

## Auth Server Persistence Design (`auth-db`)

The following rules apply to `auth-core` and the User Management Server:

1. **Entity Design & BaseEntity**:
   - All server domain entities (`User`, `Role`, `Permission`, `Tenant`, `UserRole`, `RolePermission`, `LoginAttempt`, `PasswordHistory`, `RefreshToken`, `UserSession`) extend `BaseEntity`.
   - Inherited auditing and tenancy fields:
     - `id`: `Long`, `GenerationType.IDENTITY`.
     - `createdAt` / `updatedAt`: `LocalDateTime` audited automatically.
     - `createdBy` / `updatedBy`: `String` audited via `SpringSecurityAuditorAware`.
     - `active`: `Boolean` default `true`.
     - `deleted`: `Boolean` default `false` (soft delete).
     - `tenantId`: `Long` (`@Column(name = "tenant_id")`).
2. **Naming Conventions**:
   - Database tables and columns use `snake_case` (e.g., `refresh_tokens`, `login_attempts`, `tenant_id`).
3. **Lazy Loading by Default**:
   - Always specify `FetchType.LAZY` for `@ManyToOne`, `@OneToOne`, and `@OneToMany` relationships.
   - Prevent N+1 problems using `@EntityGraph` or `JOIN FETCH` queries.
4. **Transactions**:
   - Annotate mutating service methods with `@Transactional`.
   - Annotate read-only query methods with `@Transactional(readOnly = true)`.

---

## Flyway Migration Separation

1. **Auth Server Migrations**:
   - Reside in `auth-service/src/main/resources/db/migration/auth` or the server application resources.
   - Executed only by the User Management Server.
2. **Client Service Migrations**:
   - LMS migrations reside in `lms/src/main/resources/db/migration/lms`.
   - Executed against `lms-db` during LMS application startup.
   - The two migration histories (`flyway_schema_history`) are completely independent.
