# Testing Strategy & Quality Guidelines

## Testing Pyramid

The project maintains tests at three primary levels:

1. **Unit Tests (Core Business Logic)**:
   - Reside in `src/test/java` across each module.
   - Use JUnit 5 (`org.junit.jupiter.api.*`) and Mockito (`@ExtendWith(MockitoExtension.class)`).
   - Mock all repository and external service dependencies to verify business rules in isolation.
2. **Slice Tests (Controllers & Repositories)**:
   - Use `@WebMvcTest` in `auth-service` to test request validation, security filters, and exception handling without bootstrapping the entire application context.
   - Use `@DataJpaTest` in `auth-core` to test custom repository query methods and entity auditing.
3. **Integration Tests (End-to-End Workflows)**:
   - Use `@SpringBootTest` in `auth-demo` or `auth-security` to verify filter chains, token issue/refresh cycles, and Spring Boot auto-configuration bootstrapping.

---

## Tenant Isolation Test Scenarios

Because `auth-parent` is a multi-tenant platform, tenant isolation tests are mandatory for any changes to repositories, security filters, or service lookups:

1. **Authentication Boundary**:
   - Verify that credentials valid for Tenant A produce an authentication failure when presented under Tenant B's context.
2. **Data Boundary**:
   - Seed entities for Tenant 1 and Tenant 2. Verify that queries executed with `TenantContext.setTenantId(1L)` never return records belonging to Tenant 2.
3. **JWT Boundary**:
   - Verify that an access token issued with `tenantId: 1` fails authorization if presented against an API endpoint requiring Tenant 2 context.

---

## Security & Authentication Mocking

1. **Mocking Authenticated Principals**:
   - For controller unit/slice tests, inject mock security principals:
     ```java
     @Test
     @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
     void testAdminEndpointAccess() throws Exception { ... }
     ```
   - For tests requiring the custom `AuthUserPrincipal` with `tenantId`:
     ```java
     var principal = new AuthUserPrincipal(1L, "user@tenant.com", "hash", 100L, List.of("ROLE_USER"), List.of("READ"));
     var auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
     SecurityContextHolder.getContext().setAuthentication(auth);
     ```
2. **Tenant Cleanup**:
   - Tests that manipulate `TenantContext` must clean up in an `@AfterEach` method:
     ```java
     @AfterEach
     void tearDown() {
         TenantContext.clear();
         SecurityContextHolder.clearContext();
     }
     ```

---

## Test Execution Commands

```powershell
# Run all tests across all modules
.\mvnw.cmd test

# Run tests only for a specific module
.\mvnw.cmd test -pl auth-core

# Run a specific test class
.\mvnw.cmd test -pl auth-service -Dtest=AuthControllerTest

# Run tests matching a pattern
.\mvnw.cmd test -Dtest=*TenantIsolationTest
```

---

## Best Practices & Style

1. **Fluent Assertions**:
   - Prefer AssertJ (`org.assertj.core.api.Assertions.assertThat`) over standard JUnit assertions for readable, descriptive assertion messages.
2. **Determinism**:
   - Tests must never rely on shared mutable state, specific execution order, or hardcoded timestamps.
   - Use fixed test clocks or explicit `Instant`/`LocalDateTime` parameters when testing time-dependent token expiry and account lockout durations.
