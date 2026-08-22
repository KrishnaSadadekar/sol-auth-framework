package sol.auth.service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sol.auth.core.dto.ChangePasswordRequest;
import sol.auth.security.principal.AuthUserPrincipal;
import sol.auth.service.service.AuthApplicationService;

@RestController
@Validated
@RequestMapping("/api/v1/password")
public class PasswordController {

    private final AuthApplicationService authApplicationService;

    public PasswordController(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }

    @PostMapping("/change")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @RequestBody @Valid ChangePasswordRequest request) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        authApplicationService.changePassword(principal.getUser().getUsername(), request);

        return ResponseEntity.noContent().build();
    }

}
