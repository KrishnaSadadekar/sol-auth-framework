package sol.auth.core.service;

import sol.auth.core.entity.User;
import sol.auth.core.enums.LoginStatus;

public interface LoginAttemptService {

    void recordSuccess(User user,
            String ipAddress,
            String userAgent);

    void recordFailure(String username,
            String ipAddress,
            String userAgent,
            LoginStatus status);

    long getFailedAttempts(String username);

    
}