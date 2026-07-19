package sol.auth.core.service;

import java.util.Optional;

import sol.auth.core.dto.RegisterRequest;
import sol.auth.core.entity.User;

public interface UserService {
    User register(RegisterRequest request);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User create(User user);

    Optional<User> findByLoginId(String loginId);

}
