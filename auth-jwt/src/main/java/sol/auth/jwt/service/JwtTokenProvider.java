package sol.auth.jwt.service;

import java.time.Instant;

import sol.auth.core.entity.User;

public interface JwtTokenProvider {

    String generateAccessToken(User user);

    boolean validateAccessToken(String token);

    String getUsername(String token);

    Long getUserId(String token);

    Instant getExpiration(String token);
}
