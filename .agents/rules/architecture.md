# Architecture & Modular Design Guidelines

## System Pattern: Centralized Identity Provider (SSO) & Resource Servers

The architecture mirrors enterprise identity solutions (such as **Keycloak** or **Auth0**):

```
       ┌────────────────────────────────────────────────────────┐
       │             User Management / Auth Server              │
       │                                                        │
       │   - Dedicated Database: auth-db                        │
       │   - Identity, Roles, Tenants, Password Policies        │
       │   - SSO Token Issuer (JWT / JWKS)                      │
       │   - Shared Platform Services: Email (SMTP), SMS        │
       └───────────────────────────┬────────────────────────────┘
                                   │
                    Tokens / JWKS  │  REST APIs / Feign SDK
         ┌─────────────────────────┼─────────────────────────┐
         ▼                         ▼                         ▼
   ┌───────────┐             ┌───────────┐             ┌───────────┐
   │    LMS    │             │    ERP    │             │    HR     │
   │ (Service) │             │ (Service) │             │ (Service) │
   │           │             │           │             │           │
   │  lms-db   │             │  erp-db   │             │   hr-db   │
   └───────────┘             └───────────┘             └───────────┘
```

---

## Architectural Boundaries & Anti-Patterns

### 1. The Shared Database Anti-Pattern (STRICTLY FORBIDDEN)
- **Problem**: Client microservices (like `lms`) must **NEVER** connect to `auth-db`. Having LMS boot a secondary `authDataSource` or map auth JPA entities breaks domain boundaries, tightly couples releases, and violates the microservices Database-Per-Service pattern.
- **Rule**:
  - `auth-db` is private and accessible **only** to the User Management Server (`auth-service` / `auth-demo`).
  - LMS connects **only** to `lms-db`.
  - ERP connects **only** to `erp-db`.
  - HR connects **only** to `hr-db`.

### 2. Client vs. Server Starters
- **Server Application (`auth-service` / `auth-demo`)**:
  - Contains entities (`User`, `Role`, `Tenant`, `BaseEntity`), Spring Data repositories, and connects to `auth-db`.
  - Handles user registration, credentials check, password hashing, and token issuance.
- **Client Starter (`auth-client-starter`)**:
  - Lightweight dependency included by consumer microservices (LMS, ERP, HR).
  - Automatically configures:
    1. Spring Security as a **Stateless JWT Resource Server** (validates tokens locally using the Auth Server's public key or secret with zero database lookups).
    2. `TenantFilter` / `TenantContext` to propagate `tenantId` from JWT/headers.
    3. Feign / HTTP Client SDK (`UserManagementClient`) to call central services (e.g., sending emails, SMS, or looking up profiles).
  - **Contains ZERO JPA entities, ZERO repositories, and ZERO DataSource configuration for `auth-db`!**

---

## Single Sign-On (SSO) Flow

1. **User Authentication**:
   - The user/client authenticates against the User Management Server (`POST /api/v1/auth/login`).
   - The server validates credentials against `auth-db` and returns a digitally signed JWT access token and refresh token.
2. **Resource Server Access**:
   - The client passes `Authorization: Bearer <token>` to LMS, ERP, or HR.
   - The resource server validates the JWT signature statelessly using the Auth Server's public key (or secret key / JWKS endpoint).
   - Once validated, the user is authenticated in the resource server's `SecurityContext` with their `userId`, `roles`, `permissions`, and `tenantId`.
   - **No round-trip to `auth-db` is performed.**

---

## Centralized Platform Services (Email & SMS)

To prevent reimplementing email and SMS delivery in every downstream microservice:

1. **Central Ownership**:
   - The User Management platform hosts the `EmailService` (SMTP, Thymeleaf/Freemarker templates) and `SmsService` (Twilio/AWS SNS/SMS Gateway).
2. **Consumption by Downstream Microservices**:
   - LMS, ERP, and HR do not configure their own SMTP credentials.
   - Downstream services trigger notifications by calling the User Management API:
     ```
     POST /api/v1/communications/email
     POST /api/v1/communications/sms
     ```
   - Alternatively, via asynchronous message queues (Kafka / RabbitMQ).
   - In Java applications, this call is wrapped into a clean one-line method via `auth-client-starter`:
     ```java
     emailClient.sendTemplatedEmail(new EmailRequest(...));
     ```

---

## Multi-Tenancy Strategy

1. **Shared IAM Multi-Tenancy**:
   - The User Management Server manages tenants via `Tenant` entity and tags all users with `tenantId`.
   - The issued JWT includes `tenantId` in its payload claims.
2. **Propagation to Downstream Services**:
   - When a request reaches LMS, `auth-client-starter`'s `TenantFilter` extracts `tenantId` directly from the validated JWT claims and stores it in `TenantContext`.
   - LMS uses `TenantContext.getTenantId()` to scope its own business queries in `lms-db`.
