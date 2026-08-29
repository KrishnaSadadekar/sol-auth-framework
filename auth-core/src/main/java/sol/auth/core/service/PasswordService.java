package sol.auth.core.service;

import sol.auth.core.dto.ChangePasswordRequest;
import sol.auth.core.entity.User;

public interface PasswordService {
    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);

    User changePassword(User user, ChangePasswordRequest request);

    void validatePassword(String password);
}
