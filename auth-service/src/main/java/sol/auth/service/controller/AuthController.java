package sol.auth.service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sol.auth.core.dto.ChangePasswordRequest;
import sol.auth.core.dto.LoginRequest;
import sol.auth.core.dto.RegisterRequest;
import sol.auth.service.dto.AuthResponse;
import sol.auth.service.dto.LogoutRequest;
import sol.auth.service.dto.RefreshTokenRequest;
import sol.auth.service.dto.UserSummaryResponse;
import sol.auth.service.service.AuthApplicationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import sol.auth.security.principal.AuthUserPrincipal;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

    private final AuthApplicationService authApplicationService;

    public AuthController(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authApplicationService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authApplicationService.register(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(authApplicationService.refresh(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody(required = false) LogoutRequest request) {
        if (request != null) {
            authApplicationService.logout(request.getRefreshToken(), request.getUserId());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserSummaryResponse> me(
            @AuthenticationPrincipal AuthUserPrincipal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(
                authApplicationService.me(principal));
    }

}
