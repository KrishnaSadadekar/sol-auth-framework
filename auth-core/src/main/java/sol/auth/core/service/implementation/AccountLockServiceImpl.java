package sol.auth.core.service.implementation;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import sol.auth.core.entity.User;
import sol.auth.core.event.UserLockedEvent;
import sol.auth.core.repository.UserRepository;
import sol.auth.core.service.AccountLockService;
import sol.auth.core.service.LoginAttemptService;

@Service
public class AccountLockServiceImpl implements AccountLockService {

    @Value("${auth.security.max-failed-login-attempts:5}")
    private int maxFailedAttempts;

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;
    private final ApplicationEventPublisher eventPublisher;

    public AccountLockServiceImpl(UserRepository userRepository,
            LoginAttemptService loginAttemptService,
            ApplicationEventPublisher eventPublisher) {

        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void processFailedLogin(User user) {

        long failedAttempts = loginAttemptService.getFailedAttempts(user.getUsername());

        if (failedAttempts >= maxFailedAttempts) {

            user.setAccountLocked(true);
            user.setAccountLockedAt(LocalDateTime.now());

            userRepository.save(user);
            eventPublisher.publishEvent(new UserLockedEvent(user));
        }
    }

    @Override
    public void processSuccessfulLogin(User user) {

        if (user.getFailedLoginAttempts() > 0) {
            user.setFailedLoginAttempts(0);
            user.setAccountLockedAt(null);
            userRepository.save(user);
        }
    }
}
