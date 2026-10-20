package sol.auth.client.jwt;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import sol.auth.client.config.AuthClientProperties;
import sol.auth.client.principal.AuthUserPrincipal;

public class JwtValidator {

    private static final Logger log = LoggerFactory.getLogger(JwtValidator.class);

    private final AuthClientProperties properties;
    private final SecretKey signingKey;

    public JwtValidator(AuthClientProperties properties) {
        this.properties = properties;
        this.signingKey = createSigningKey(properties.getSecret());
    }

    public Optional<AuthUserPrincipal> validateAndExtract(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject();
            Long userId = extractLong(claims, "uid");
            if (userId == null) {
                userId = extractLong(claims, "userId");
            }
            Long tenantId = extractLong(claims, "tid");
            if (tenantId == null) {
                tenantId = extractLong(claims, "tenantId");
            }

            List<String> roles = extractStringList(claims, "roles");
            if (roles.isEmpty()) {
                Object singleRole = claims.get("role");
                if (singleRole != null) {
                    roles = new ArrayList<>(List.of(singleRole.toString()));
                }
            }
            List<String> permissions = extractStringList(claims, "permissions");

            AuthUserPrincipal principal = AuthUserPrincipal.builder()
                    .userId(userId)
                    .username(username)
                    .tenantId(tenantId)
                    .roles(roles)
                    .permissions(permissions)
                    .build();

            return Optional.of(principal);
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("JWT verification failed: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    private Long extractLong(Claims claims, String key) {
        Object val = claims.get(key);
        if (val instanceof Number number) {
            return number.longValue();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<String> extractStringList(Claims claims, String key) {
        Object val = claims.get(key);
        if (val instanceof Collection<?> collection) {
            List<String> list = new ArrayList<>();
            for (Object item : collection) {
                if (item != null) {
                    list.add(item.toString());
                }
            }
            return list;
        }
        return new ArrayList<>();
    }

    private SecretKey createSigningKey(String secret) {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalArgumentException("auth.client.secret must be at least 32 bytes for HS256 validation");
        }
        return Keys.hmacShaKeyFor(secretBytes);
    }
}
