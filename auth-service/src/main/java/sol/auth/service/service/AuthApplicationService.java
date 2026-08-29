package sol.auth.service.service;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import sol.auth.core.dto.ChangePasswordRequest;
import sol.auth.core.dto.LoginRequest;
import sol.auth.core.dto.RegisterRequest;
import sol.auth.core.entity.PasswordHistory;
import sol.auth.core.entity.RefreshToken;
import sol.auth.core.entity.User;
import sol.auth.core.event.UserPasswordChangedEvent;
import sol.auth.core.exception.InvalidCredentialsException;
import sol.auth.core.exception.PasswordReuseException;
import sol.auth.core.repository.PasswordHistoryRepository;
import sol.auth.core.service.AuthenticationService;
import sol.auth.core.service.PasswordService;
import sol.auth.core.service.RegistrationService;
import sol.auth.core.service.UserService;
import sol.auth.jwt.service.JwtTokenProvider;
import sol.auth.jwt.service.RefreshTokenService;
import sol.auth.security.principal.AuthUserPrincipal;
import sol.auth.service.dto.AuthResponse;
import sol.auth.service.dto.AuthTokenResponse;
import sol.auth.service.dto.UserSummaryResponse;

@Service
public class AuthApplicationService {

    private final AuthenticationService authenticationService;
    private final RegistrationService registrationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final PasswordService passwordService;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final HttpServletRequest httpRequest;

    public AuthApplicationService(AuthenticationService authenticationService,
            RegistrationService registrationService,
            RefreshTokenService refreshTokenService,
            JwtTokenProvider jwtTokenProvider,
            UserService userService, PasswordService passwordService,
            PasswordHistoryRepository passwordHistoryRepository, ApplicationEventPublisher eventPublisher,
            HttpServletRequest request) {
        this.authenticationService = authenticationService;
        this.registrationService = registrationService;
        this.refreshTokenService = refreshTokenService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.passwordService = passwordService;
        this.passwordHistoryRepository = passwordHistoryRepository;
        this.eventPublisher = eventPublisher;
        this.httpRequest = request;
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = authenticationService.login(request);
        return issueTokensForUser(user);
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        User user = registrationService.register(request);
        return issueTokensForUser(user);
    }

    @Transactional
    public AuthResponse refresh(String refreshToken) {
        if (!refreshTokenService.isValid(refreshToken)) {
            throw new InvalidCredentialsException("Invalid or expired refresh token");
        }

        RefreshToken existingToken = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidCredentialsException("Refresh token not found"));

        User user = userService.findById(existingToken.getUserId());

        refreshTokenService.revoke(refreshToken);
        return issueTokensForUser(user);
    }

    @Transactional
    public void logout(String refreshToken, Long userId) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            refreshTokenService.revoke(refreshToken);
            return;
        }

        if (userId != null) {
            refreshTokenService.revokeAllByUserId(userId);
        }
    }

    @Transactional(readOnly = true)
    public UserSummaryResponse me(AuthUserPrincipal principal) {

        User user = principal.getUser();

        return toUserSummary(user);
    }

    private AuthResponse issueTokensForUser(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.issueToken(user);

        AuthTokenResponse tokenResponse = new AuthTokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefreshToken(refreshToken.getToken());
        tokenResponse.setExpiresAt(jwtTokenProvider.getExpiration(accessToken));

        AuthResponse response = new AuthResponse();
        response.setToken(tokenResponse);
        response.setUser(toUserSummary(user));
        return response;
    }

    private UserSummaryResponse toUserSummary(User user) {
        UserSummaryResponse response = new UserSummaryResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        return response;
    }

    @Transactional
    public void changePassword(
            String username,
            ChangePasswordRequest request) {

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        // 1. validate password history
        validatePasswordHistory(user.getId(), request);

        // 2. Validate current password + policy + generate new hash
        User savedUser = passwordService.changePassword(user, request);
        user.setPassword(savedUser.getPassword());

        // 3. Save the new password hash into history
        savePasswordHistory(user);

        eventPublisher.publishEvent(new UserPasswordChangedEvent(savedUser, httpRequest.getRemoteAddr(),
                httpRequest.getHeader("User-Agent")));

    }

    // Reset password
    public void resetPassword(String username, ChangePasswordRequest request) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));
        passwordService.validatePassword(request.getCurrentPassword());
        validatePasswordHistory(user.getId(), request);
        savePasswordHistory(user);
        eventPublisher.publishEvent(new UserPasswordChangedEvent(user, httpRequest.getRemoteAddr(),
                httpRequest.getHeader("User-Agent")));

    }

    // Validate password history
    private void validatePasswordHistory(Long id, ChangePasswordRequest request) {
        // 1. Get password history
        List<PasswordHistory> histories = passwordHistoryRepository
                .findByUserIdOrderByCreatedAtDesc(id);

        // 2. Check password reuse
        boolean reused = histories.stream()
                .anyMatch(history -> passwordService.matches(
                        request.getNewPassword(),
                        history.getPasswordHash()));

        if (reused) {
            throw new PasswordReuseException(
                    "New password was previously used");
        }
    }

    // Save the new password hash into history
    private PasswordHistory savePasswordHistory(User user) {
        PasswordHistory history = new PasswordHistory();
        history.setUser(user);
        history.setPasswordHash(user.getPassword());
        return passwordHistoryRepository.save(history);

    }

    public AuthResponse resetPassword(String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        AuthResponse authResponse = issueTokensForUser(user);
        authResponse.setResetEmailLink("reset password link ");

        return authResponse;

    }

}
