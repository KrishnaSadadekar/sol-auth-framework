package sol.auth.core.service.implementation;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import sol.auth.core.entity.User;
import sol.auth.core.repository.UserRepository;
import sol.auth.core.service.AccountLockService;
import sol.auth.core.service.LoginAttemptService;

@Service
public class AccountLockServiceImpl implements AccountLockService {

    private static final int MAX_FAILED_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    public AccountLockServiceImpl(UserRepository userRepository,
            LoginAttemptService loginAttemptService) {

        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void processFailedLogin(User user) {

        long failedAttempts = loginAttemptService.getFailedAttempts(user.getUsername());

        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {

            user.setAccountLocked(true);
            user.setAccountLockedAt(LocalDateTime.now());

            userRepository.save(user);
        }
    }

    @Override
    public void processSuccessfulLogin(User user) {

        // We'll improve this later
    }
}