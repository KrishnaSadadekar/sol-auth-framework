# Git & Collaboration Guidelines

## Commit Message Conventions

This project enforces the **Conventional Commits** specification:

```
<type>(<scope>): <short description in present tense>

[optional body describing problem, solution, and rationale]

[optional footer(s), e.g., Closes #123]
```

### Commit Types

| Type | When to Use | Example |
| :--- | :--- | :--- |
| `feat` | Adding a new feature, endpoint, or configuration | `feat(jwt): implement refresh token rotation and revocation` |
| `fix` | Correcting a bug, security flaw, or incorrect behavior | `fix(security): clear TenantContext in finally block to prevent thread leak` |
| `refactor`| Code restructuring without changing functional behavior | `refactor(core): extract password validation into pluggable strategy` |
| `test` | Adding or updating unit, slice, or integration tests | `test(tenant): add test cases verifying cross-tenant access rejection` |
| `docs` | Modifying documentation, API contracts, or markdown files| `docs(api): document /api/v1/auth/refresh contract and error codes` |
| `chore` | Build configuration, dependency upgrades, repository maintenance | `chore(deps): update lombok version to 1.18.38` |

### Scopes
Recommended scopes:
- `core`: Domain entities, repositories, core services
- `security`: Spring Security configuration, filters, principals
- `jwt`: Token generation, validation, refresh token handling
- `service`: REST controllers, DTOs, API exception handlers
- `autoconfigure`: Auto-configuration, properties, multi-datasource beans
- `starter`: Starter dependency configuration
- `lms`: LMS consumer integration
- `db`: Flyway migrations, database scripts

---

## Branch Naming Strategy

Branches should be named with the type prefix followed by a concise, kebab-case description:

- `feature/<feature-name>` (e.g., `feature/refresh-token-rotation`)
- `fix/<bug-description>` (e.g., `fix/tenant-context-leak`)
- `refactor/<refactoring-scope>` (e.g., `refactor/security-filter-chain`)
- `chore/<task-name>` (e.g., `chore/upgrade-spring-boot`)

---

## Security Hygiene & Secret Protection

1. **Zero Secret Commit Policy**:
   - **Never commit production passwords, private keys, database credentials, or secret keys to git**.
   - Use environment variables with sensible local defaults in configuration files:
     ```yaml
     jwt:
       secret: ${JWT_SECRET:my-local-development-secret-key-at-least-32-bytes}
     ```
2. **Ignored Files**:
   - Verify `.gitignore` excludes: `target/`, `*.class`, `.idea/`, `.vscode/`, `.env`, and local credentials.

---

## Agent Pre-Commit Quality Gate

Before completing any task or staging changes, the agent must verify:
1. **Compilation**: `.\mvnw.cmd clean compile` completes with zero compilation errors.
2. **Tests**: Existing tests pass without regression.
3. **No Unintended Edits**: Unrelated files, temporary debug prints, or unwanted file reformats are avoided.
4. **Architectural Integrity**: Downward module dependency rules are strictly preserved.
