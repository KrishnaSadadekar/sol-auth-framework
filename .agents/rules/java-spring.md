# Java 21 & Spring Boot Standards

## Language Level: Java 21

1. **Modern Language Constructs**:
   - Utilize Java 21 features where beneficial: records for immutable DTOs and internal value objects, pattern matching for `instanceof` and `switch`, enhanced switch expressions, and text blocks for multi-line SQL/JSON.
   - Use `var` only when the type is immediately obvious from the right-hand assignment (e.g., `var user = new User()`), avoiding ambiguity for complex return types.
2. **Null Safety**:
   - Prefer `java.util.Optional<T>` for method return types where a value may be absent (e.g., repository finders).
   - Never return `null` for collections or arrays; return `List.of()`, `Set.of()`, or `Collections.emptyList()`.
   - Never use `Optional` as method parameters or class field types.

---

## Spring Boot 3.5.x & Jakarta Conventions

1. **Jakarta EE Namespace**:
   - Strictly use the `jakarta.*` packages (`jakarta.persistence.*`, `jakarta.servlet.*`, `jakarta.validation.*`, `jakarta.annotation.*`). Never import legacy `javax.*` packages.
2. **Dependency Injection**:
   - **Always use constructor injection**:
     ```java
     @Service
     public class UserServiceImpl implements UserService {
         private final UserRepository userRepository;
         private final PasswordService passwordService;

         public UserServiceImpl(UserRepository userRepository, PasswordService passwordService) {
             this.userRepository = userRepository;
             this.passwordService = passwordService;
         }
     }
     ```
   - **Strictly forbid field injection** (`@Autowired` on private fields).
   - Alternatively, use Lombok `@RequiredArgsConstructor` with `final` fields.
3. **Bean Registration & Auto-Configuration**:
   - Service classes in `auth-core` must be annotated with `@Service` or registered explicitly in configuration.
   - Beans intended for external library consumption in `auth-autoconfigure` must use `@ConditionalOnMissingBean` so client applications can supply custom implementations.
   - Register auto-configuration classes in `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.

---

## Lombok Best Practices

1. **Entities vs. DTOs**:
   - **On JPA Entities**: Use `@Getter` and `@Setter`. **Do NOT use `@Data` or `@EqualsAndHashCode` on JPA entities**, as it generates automatic `hashCode()` and `equals()` implementations involving all fields, which triggers premature loading of lazy associations and breaks Set collections across entity states.
   - **On DTOs / Models**: `@Data`, `@Builder`, `@NoArgsConstructor`, and `@AllArgsConstructor` are encouraged.
2. **Constructors**:
   - Prefer `@RequiredArgsConstructor` or explicit constructors over `@AllArgsConstructor` on services to avoid unintentional parameter order bugs.

---

## Exception Handling & Logging

1. **Domain Exceptions**:
   - Create domain-specific exceptions extending `RuntimeException` in `sol.auth.core.exception` (e.g., `InvalidCredentialsException`, `AccountLockedException`, `UserAlreadyExistsException`, `TenantResolutionException`).
   - Do not leak internal persistence details, SQL exceptions, or stack traces to callers.
2. **Logging Conventions**:
   - Use SLF4J (either via Lombok `@Slf4j` or `LoggerFactory.getLogger(Class)`).
   - Include contextual MDC tags: `correlationId` and `tenantId` (ensured by `CorrelationIdFilter` and `TenantFilter`).
   - Log format in `application.yml`:
     ```yaml
     logging:
       pattern:
         console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] [%X{correlationId}] [%X{tenantId}] %-5level %logger{36} - %msg%n"
     ```
   - **Security Logging**: Never log plain-text passwords, authorization headers, raw JWT tokens, or sensitive PII.

---

## Asynchronous & Event-Driven Processing

1. **Domain Events**:
   - Use Spring's `ApplicationEventPublisher` to publish state transition events:
     ```java
     eventPublisher.publishEvent(new UserRegisteredEvent(this, user));
     ```
2. **Event Listeners**:
   - Mark non-blocking side-effects (such as audit logging, email dispatch, or metrics collection) with `@Async` and `@EventListener` (or `@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)`).
   - Ensure an asynchronous task executor is configured (enabled via `@EnableAsync`).
