package sol.auth.security.service.Implementation;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sol.auth.core.dto.ChangePasswordRequest;
import sol.auth.core.entity.User;
import sol.auth.core.exception.InvalidCredentialsException;
import sol.auth.core.service.PasswordService;
import sol.auth.security.service.PasswordPolicyValidator;

@Service
public class BCryptPasswordService implements PasswordService {

    private final PasswordEncoder passwordEncoder;

    private final PasswordPolicyValidator passwordPolicyValidator;

    public BCryptPasswordService(PasswordEncoder passwordEncoder,
            PasswordPolicyValidator passwordPolicyValidator) {
        this.passwordPolicyValidator = passwordPolicyValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String rawPassword) {

        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {

        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public User changePassword(
            User user,
            ChangePasswordRequest request) {

        // 1. Verify current password
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new InvalidCredentialsException(
                    "Current password is incorrect");
        }

        // 2. Validate new password policy
        passwordPolicyValidator.validate(
                request.getNewPassword());

        // 3. Make sure new password isn't current password
        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword())) {

            throw new InvalidCredentialsException(
                    "New password must be different from current password");
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()));
        return user;

    }

}
