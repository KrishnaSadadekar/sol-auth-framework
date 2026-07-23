package sol.auth.jwt.service.implementation;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import sol.auth.core.entity.User;
import sol.auth.jwt.configuration.JwtTokenProperties;
import sol.auth.jwt.service.JwtTokenProvider;

@Service
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private final JwtTokenProperties properties;
    private final SecretKey signingKey;

    public JwtTokenProviderImpl(JwtTokenProperties properties) {
        this.properties = properties;
        this.signingKey = createSigningKey(properties.getSecret());
    }

    @Override
    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(properties.getAccessTokenTtlSeconds());

        return Jwts.builder()
                .issuer(properties.getIssuer())
                .subject(user.getUsername())
                .claim("uid", user.getId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

    @Override
    public boolean validateAccessToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    @Override
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    @Override
    public Long getUserId(String token) {
        Object value = parseClaims(token).get("uid");
        if (value instanceof Number number) {
            return number.longValue();
        }
        return null;
    }

    @Override
    public Instant getExpiration(String token) {
        return parseClaims(token).getExpiration().toInstant();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey createSigningKey(String secret) {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalArgumentException(
                    "auth.jwt.secret must be at least 32 bytes for HS256 signing");
        }
        return Keys.hmacShaKeyFor(secretBytes);
    }
}
