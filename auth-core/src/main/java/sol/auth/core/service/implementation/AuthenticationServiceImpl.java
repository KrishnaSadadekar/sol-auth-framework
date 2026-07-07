package sol.auth.core.service.implementation;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sol.auth.core.dto.LoginRequest;
import sol.auth.core.entity.User;
import sol.auth.core.enums.LoginStatus;
import sol.auth.core.exception.AccountLockedException;
import sol.auth.core.exception.InvalidCredentialsException;
import sol.auth.core.exception.UserDisabledException;
import sol.auth.core.repository.UserRepository;
import sol.auth.core.service.AuthenticationService;
import sol.auth.core.service.LoginAttemptService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final LoginAttemptService loginAttemptService;
    private final AccountLockServiceImpl accountLockService;

    public AuthenticationServiceImpl(UserRepository userRepository,
            AccountLockServiceImpl accountLockService,

            LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.accountLockService = accountLockService;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public User login(LoginRequest request) {

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElse(null);

        // User not found
        if (user == null) {
            loginAttemptService.recordFailure(
                    request.getUsername(),
                    null,
                    null,
                    LoginStatus.USER_NOT_FOUND);
            accountLockService.processFailedLogin(user);
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // User inactive
        if (!Boolean.TRUE.equals(user.getActive())) {

            loginAttemptService.recordFailure(
                    user.getUsername(),
                    null,
                    null,
                    LoginStatus.USER_DISABLED);

            throw new UserDisabledException("User account is inactive");
        }

        // Account locked
        if (Boolean.TRUE.equals(user.getAccountLocked())) {

            loginAttemptService.recordFailure(
                    user.getUsername(),
                    null,
                    null,
                    LoginStatus.ACCOUNT_LOCKED);

            throw new AccountLockedException("Account is locked");
        }

        // User deleted
        if (Boolean.TRUE.equals(user.getDeleted())) {

            loginAttemptService.recordFailure(
                    user.getUsername(),
                    null,
                    null,
                    LoginStatus.USER_DISABLED);

            throw new UserDisabledException("User account is deleted");
        }

        // Invalid password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {

            loginAttemptService.recordFailure(
                    user.getUsername(),
                    null,
                    null,
                    LoginStatus.INVALID_PASSWORD);

            throw new InvalidCredentialsException("Invalid username or password");
        }
        accountLockService.processSuccessfulLogin(user);
        // Successful login
        loginAttemptService.recordSuccess(
                user,
                null,
                null);

        return user;
    }
}