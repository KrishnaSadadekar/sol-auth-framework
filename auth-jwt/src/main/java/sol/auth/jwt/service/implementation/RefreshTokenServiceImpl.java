package sol.auth.jwt.service.implementation;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sol.auth.core.entity.RefreshToken;
import sol.auth.core.entity.User;
import sol.auth.core.repository.RefreshTokenRepository;
import sol.auth.jwt.configuration.JwtTokenProperties;
import sol.auth.jwt.service.RefreshTokenService;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final int REFRESH_TOKEN_SIZE_BYTES = 48;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProperties properties;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,
            JwtTokenProperties properties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.properties = properties;
    }

    @Override
    @Transactional
    public RefreshToken issueToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(generateSecureToken())
                .userId(user.getId())
                .expiresAt(LocalDateTime.now().plusSeconds(
                        properties.getRefreshTokenTtlSeconds()))
                .revoked(false)
                .build();

        refreshToken.setActive(true);
        refreshToken.setDeleted(false);

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public boolean isValid(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if (refreshToken.isEmpty()) {
            return false;
        }

        RefreshToken storedToken = refreshToken.get();
        return Boolean.TRUE.equals(storedToken.getActive())
                && Boolean.FALSE.equals(storedToken.getDeleted())
                && Boolean.FALSE.equals(storedToken.getRevoked())
                && storedToken.getExpiresAt() != null
                && storedToken.getExpiresAt().isAfter(LocalDateTime.now());
    }

    @Override
    @Transactional
    public void revoke(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(this::revokeToken);
    }

    @Override
    @Transactional
    public void revokeAllByUserId(Long userId) {
        List<RefreshToken> userTokens = refreshTokenRepository.findByUserId(userId);
        userTokens.forEach(this::revokeToken);
        refreshTokenRepository.saveAll(userTokens);
    }

    private void revokeToken(RefreshToken token) {
        token.setRevoked(true);
        token.setActive(false);
    }

    private String generateSecureToken() {
        byte[] randomBytes = new byte[REFRESH_TOKEN_SIZE_BYTES];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
