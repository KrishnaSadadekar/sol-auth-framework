# Security, SSO & Authentication Standards

## SSO & Token Verification Model

The security architecture operates on a **Single Sign-On (SSO) & OAuth2/JWT Resource Server** model:

```
[ Client Browser / Frontend ]
          │
          │ 1. Login (username/password)
          ▼
┌───────────────────────────────┐
│ User Management / Auth Server │ ── Issues Signed JWT (Access + Refresh)
└───────────────────────────────┘
          │
          │ 2. Request with Bearer JWT
          ▼
┌───────────────────────────────┐
│     LMS / ERP / HR App        │ ── Statelessly validates JWT with Auth Server Public Key
│      (Resource Server)        │    (ZERO database lookup!)
└───────────────────────────────┘
```

---

## 1. Auth Server Security (Identity Provider)

1. **Authentication & Issuance**:
   - Authenticates credentials against `auth-db`.
   - Uses `BCryptPasswordEncoder` (strength 10+) for secure password storage.
   - Enforces password strength rules via `PasswordPolicy` and prevents password reuse via `PasswordHistory`.
   - Protects against brute-force attacks via `LoginAttemptService` and `AccountLockService`.
2. **Token Signing & Keys**:
   - **Asymmetric Signing (RSA)**: The Auth Server signs JWT tokens using a private RSA key.
   - Exposes public keys via a standard JWKS endpoint (`/.well-known/jwks.json`) or shared public key property.
   - Access Token Claims:
     - `sub`: User ID or unique subject.
     - `tenantId`: Tenant ID.
     - `roles`: Assigned roles (`ROLE_USER`, `ROLE_ADMIN`, etc.).
     - `permissions`: Fine-grained permission strings.
     - `iss`, `iat`, `exp`: Token lifetime metadata (default access TTL: 15 minutes).
3. **Refresh Tokens**:
   - Stored in `auth-db` (`RefreshToken` entity).
   - Invalidation & rotation upon exchange; revocation upon logout or password reset.

---

## 2. Consumer Applications Security (Resource Servers)

1. **Stateless JWT Resource Server**:
   - Consumer applications (LMS, ERP, HR) use `auth-client-starter`.
   - Configures Spring Security 6 as a stateless OAuth2 / JWT Resource Server:
     ```java
     http.csrf(AbstractHttpConfigurer::disable)
         .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
         .authorizeHttpRequests(auth -> auth
             .requestMatchers("/api/public/**", "/swagger-ui/**").permitAll()
             .anyRequest().authenticated()
         )
         .oauth2ResourceServer(oauth2 -> oauth2.jwt(...));
     ```
2. **Local Token Verification**:
   - Resource servers verify JWT tokens locally using the Auth Server's public key (or secret key / JWKS).
   - **Zero connection to `auth-db` is required.**
3. **Principal & Tenant Context**:
   - A custom JWT authentication converter maps token claims into `AuthUserPrincipal`.
   - `TenantFilter` extracts `tenantId` from the validated claims and sets it into `TenantContext` for the request lifecycle, clearing it in a `finally` block.

---

## 3. Password Policies & Lockout Thresholds

1. **Lockout Policy**:
   - Default threshold: 5 consecutive failed login attempts (`auth.security.max-failed-login-attempts`).
   - Default lockout duration: 30 minutes (`auth.security.lockout-duration-minutes`).
   - Locked accounts respond with HTTP 423 (Locked).
2. **Auditing**:
   - `SpringSecurityAuditorAware` resolves the authenticated username/ID from `SecurityContextHolder` to populate `BaseEntity` audit timestamps.
