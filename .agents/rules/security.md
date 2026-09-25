# Security & Authentication Standards

## Spring Security 6 Architecture

1. **Stateless Filter Chain**:
   - The security architecture is strictly stateless. Configure `SessionCreationPolicy.STATELESS` in `SecurityConfiguration`.
   - Never create or rely on `HttpSession`.
   - CSRF protection is disabled for stateless bearer-token REST endpoints:
     ```java
     http.csrf(AbstractHttpConfigurer::disable)
         .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
     ```
2. **Filter Placement**:
   - `JwtAuthenticationFilter` must be positioned before `UsernamePasswordAuthenticationFilter`:
     ```java
     http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
     ```
   - Unauthenticated public endpoints (e.g., `/api/v1/auth/login`, `/api/v1/auth/register`, `/api/v1/auth/refresh`, OpenAPI/Swagger endpoints) must be explicitly permitted with `.permitAll()`.
   - All other application endpoints must require authentication with `.authenticated()`.

---

## Token Lifecycle & JWT Policy

1. **Access Tokens**:
   - Short-lived lifespan (default: 15 minutes / 900 seconds).
   - Generated and validated by `JwtTokenProvider` using HMAC-SHA256 (or RSA-256 for asymmetric deployment).
   - Mandatory claims in payload:
     - `sub`: Subject identifier (user UUID or ID).
     - `tenantId`: Active tenant identifier.
     - `roles`: Assigned roles (e.g. `ROLE_ADMIN`, `ROLE_USER`).
     - `permissions`: Assigned granular permissions.
     - `iss`, `iat`, `exp`: Standard JWT claims.
2. **Refresh Tokens**:
   - Long-lived lifespan (default: 7 days / 604800 seconds).
   - Persisted in the database via `RefreshToken` entity.
   - Tied strictly to `userId` and `tenantId`.
   - **Rotation Policy**: Every successful refresh token exchange must invalidate the old refresh token and issue a newly minted pair (access token + refresh token).
   - **Revocation**: Upon `/logout` or password change, all active refresh tokens for the user must be revoked/invalidated immediately.

---

## Password Management & Policies

1. **Password Hashing**:
   - Always use `BCryptPasswordEncoder` with a minimum strength of 10.
   - Plaintext passwords must never touch the database or logs.
2. **Password Validation**:
   - Validate passwords against `PasswordPolicy` via `PasswordPolicyValidator` during registration and password reset.
   - Enforce: minimum length (e.g. 8+ characters), combination of uppercase, lowercase, numbers, and special symbols.
3. **Password History & Reuse**:
   - Store hashed passwords in `PasswordHistory`.
   - Forbid reusing any of the last *N* previous passwords (default: 3 to 5).

---

## Account Lockout & Brute-Force Protection

1. **Failed Attempt Tracking**:
   - Record every authentication failure in `LoginAttempt` through `LoginAttemptService`.
   - Trigger account locking via `AccountLockService` when failed attempts exceed `auth.security.max-failed-login-attempts` (default: 5).
2. **Lockout Policy**:
   - Lockout duration is defined by `auth.security.lockout-duration-minutes` (default: 30 minutes).
   - If an account is locked, reject requests with `AccountLockedException` (translated to HTTP 423 Locked).
   - Successful authentication must reset the consecutive failed login counter to 0.

---

## Tenant Isolation in Security Context

1. **Principal Enrichment**:
   - `AuthUserPrincipal` implements `UserDetails` and holds both user credentials and `tenantId`.
2. **Cross-Tenant Prevention**:
   - `JwtAuthenticationFilter` must verify that the `tenantId` extracted from the JWT matches the `X-Tenant-Id` header (or initialize `TenantContext` using the token's claims).
   - A valid JWT issued for Tenant A must never be accepted to authenticate actions or access resources in Tenant B.

---

## Auditing & Security Context

1. **`SpringSecurityAuditorAware`**:
   - Implement `AuditorAware<String>` (or `AuditorAware<Long>`) to populate `createdBy` and `updatedBy` in `BaseEntity` from `SecurityContextHolder.getContext().getAuthentication()`.
   - Handle unauthenticated contexts (e.g., registration or system migrations) gracefully with a fallback value (e.g., `"SYSTEM"` or `"ANONYMOUS"`).
