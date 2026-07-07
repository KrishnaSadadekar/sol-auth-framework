package sol.auth.core.service;

import sol.auth.core.entity.User;

public interface AccountLockService {

    void processFailedLogin(User user);

    void processSuccessfulLogin(User user);
}
