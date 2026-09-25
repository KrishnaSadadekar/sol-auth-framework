# Database & Persistence Guidelines

## Dual-Datasource & Persistence Unit Isolation

When integrating the auth framework into host applications (such as `lms`):

1. **Datasource Separation**:
   - Host applications configure their primary business datasource via standard `spring.datasource.*` (e.g., `lms_db`).
   - The auth framework provides its own isolated datasource configured via `auth.datasource.*` (e.g., `auth_db`).
2. **Explicit JPA Component Naming**:
   - `authDataSource`: Dedicated `DataSource` bean for authentication and user management tables.
   - `authEntityManagerFactory`: `LocalContainerEntityManagerFactoryBean` scanning `sol.auth.core.entity` with persistence unit name `"authPersistenceUnit"`.
   - `authTransactionManager`: `PlatformTransactionManager` binding transactions to `authEntityManagerFactory`.
3. **Repository Scanning**:
   - Repositories in `sol.auth.core.repository` are enabled via:
     ```java
     @EnableJpaRepositories(
         basePackages = "sol.auth.core.repository",
         entityManagerFactoryRef = "authEntityManagerFactory",
         transactionManagerRef = "authTransactionManager"
     )
     ```
   - Host applications must not mix their business repositories into the auth package scan.

---

## Entity Design & BaseEntity Conventions

1. **Inheritance & Common Fields**:
   - All domain entities in `auth-core` must extend `sol.auth.core.entity.BaseEntity`.
   - Common fields inherited from `BaseEntity`:
     - `id`: `Long`, generated via `GenerationType.IDENTITY`.
     - `createdAt` / `updatedAt`: `LocalDateTime`, populated by `@CreatedDate` and `@LastModifiedDate`.
     - `createdBy` / `updatedBy`: `String`, populated by `@CreatedBy` and `@LastModifiedBy`.
     - `active`: `Boolean` (defaults to `true`).
     - `deleted`: `Boolean` (defaults to `false`, supporting soft delete).
     - `tenantId`: `Long` (`@Column(name = "tenant_id")`).
2. **Naming Conventions**:
   - Tables and columns use lowercase `snake_case` (e.g., `login_attempts`, `refresh_tokens`, `role_permissions`).
   - Avoid reserved SQL keywords in column definitions.
3. **Relationships & Fetch Types**:
   - **Always default to `FetchType.LAZY`** for `@ManyToOne`, `@OneToOne`, and `@OneToMany` relationships. Never use `FetchType.EAGER` unless strictly necessary and documented.
   - Guard against N+1 query problems using `@EntityGraph` or `JOIN FETCH` JPQL queries in repository methods.

---

## Repository & Query Guidelines

1. **Tenant-Safe Queries**:
   - Queries retrieving tenant-scoped entities must include `tenantId`:
     ```java
     Optional<User> findByUsernameAndTenantIdAndDeletedFalse(String username, Long tenantId);
     boolean existsByEmailAndTenantIdAndDeletedFalse(String email, Long tenantId);
     ```
2. **Soft Delete Filtering**:
   - Queries must respect `deleted = false` so soft-deleted records are not exposed to active business logic.
3. **Transactions**:
   - Annotate mutating service methods with `@Transactional(transactionManager = "authTransactionManager")`.
   - Annotate read-only query methods with `@Transactional(readOnly = true)`.

---

## Schema Evolution & Migration (Flyway)

1. **Separation of Migrations**:
   - Auth framework schema scripts must reside under `classpath:db/migration/auth`.
   - Consuming applications (like `lms`) store their domain migrations under a distinct path (e.g., `classpath:db/migration/lms`).
2. **Deterministic Versioning**:
   - Name migration files with standard Flyway numbering: `V1__init_auth_schema.sql`, `V2__add_tenant_indexes.sql`.
   - Never modify an already-executed migration script. Add a new migration version instead.
