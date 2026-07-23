package sol.auth.jwt.service;

import java.util.Optional;

import sol.auth.core.entity.RefreshToken;
import sol.auth.core.entity.User;

public interface RefreshTokenService {

    RefreshToken issueToken(User user);

    Optional<RefreshToken> findByToken(String token);

    boolean isValid(String token);

    void revoke(String token);

    void revokeAllByUserId(Long userId);
}
