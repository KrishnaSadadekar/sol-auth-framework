package sol.auth.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import sol.auth.jwt.service.JwtTokenProvider;

/**
 * Security-layer facade over {@link JwtTokenProvider}.
 * Keeps {@code auth-security} classes independent of auth-jwt
 * implementation details.
 */
@Service
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (!jwtTokenProvider.validateAccessToken(token)) {
            return false;
        }
        String username = jwtTokenProvider.getUsername(token);
        return username != null && username.equals(userDetails.getUsername());
    }

    public String extractUsername(String token) {
        return jwtTokenProvider.getUsername(token);
    }

    public boolean validateToken(String token) {
        return jwtTokenProvider.validateAccessToken(token);
    }
}
