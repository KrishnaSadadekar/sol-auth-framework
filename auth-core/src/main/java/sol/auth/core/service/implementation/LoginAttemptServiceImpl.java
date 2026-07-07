package sol.auth.core.service.implementation;

import sol.auth.core.entity.LoginAttempt;
import sol.auth.core.entity.User;
import sol.auth.core.enums.LoginStatus;
import sol.auth.core.repository.LoginAttemptRepository;
import sol.auth.core.service.LoginAttemptService;

public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepository;

    public LoginAttemptServiceImpl(LoginAttemptRepository loginAttemptRepository) {
        this.loginAttemptRepository = loginAttemptRepository;
    }

    @Override
    public void recordSuccess(User user, String ipAddress, String userAgent) {
        LoginAttempt loginAttempt = LoginAttempt.builder()
                .user(user)
                .username(user.getUsername())
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .status(LoginStatus.SUCCESS)
                .build();

        loginAttemptRepository.save(loginAttempt);
    }

    @Override
    public void recordFailure(String username, String ipAddress, String userAgent, LoginStatus status) {

        LoginAttempt loginAttempt = LoginAttempt.builder()
                .username(username)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .status(status)
                  .build();

        loginAttemptRepository.save(loginAttempt);
    }

    @Override
    public long getFailedAttempts(String username) {
         return loginAttemptRepository.countByUsernameAndStatus(
            username,
            LoginStatus.INVALID_PASSWORD);
    }

}
