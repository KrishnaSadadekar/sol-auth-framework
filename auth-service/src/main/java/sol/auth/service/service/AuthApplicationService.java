package sol.auth.service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sol.auth.core.dto.ChangePasswordRequest;
import sol.auth.core.dto.LoginRequest;
import sol.auth.core.dto.RegisterRequest;
import sol.auth.core.entity.RefreshToken;
import sol.auth.core.entity.User;
import sol.auth.core.exception.InvalidCredentialsException;
import sol.auth.core.repository.UserRepository;
import sol.auth.core.service.AuthenticationService;
import sol.auth.core.service.PasswordService;
import sol.auth.core.service.RegistrationService;
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
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public AuthApplicationService(AuthenticationService authenticationService,
            RegistrationService registrationService,
            RefreshTokenService refreshTokenService,
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository, PasswordService passwordService) {
        this.authenticationService = authenticationService;
        this.registrationService = registrationService;
        this.refreshTokenService = refreshTokenService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordService = passwordService;
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

        User user = userRepository.findById(existingToken.getUserId())
                .orElseThrow(() -> new InvalidCredentialsException("User not found for refresh token"));

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

    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("USer not found"));
        userRepository.save(passwordService.changePassword(user, request));
    }

}
