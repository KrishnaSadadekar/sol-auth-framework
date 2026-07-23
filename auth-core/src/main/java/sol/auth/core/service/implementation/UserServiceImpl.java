package sol.auth.core.service.implementation;

import java.util.Optional;

import org.springframework.stereotype.Service;

import sol.auth.core.dto.RegisterRequest;
import sol.auth.core.entity.User;
import sol.auth.core.exception.UserAlreadyExistsException;
import sol.auth.core.repository.UserRepository;
import sol.auth.core.service.PasswordService;
import sol.auth.core.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final String USER_ALREADY_EXISTS_MESSAGE = "User with %s %s already exists";

    private final PasswordService passwordService;

    UserServiceImpl(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @Override
    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(
                    String.format(USER_ALREADY_EXISTS_MESSAGE, "username", request.getUsername()));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(
                    String.format(USER_ALREADY_EXISTS_MESSAGE, "email", request.getEmail()));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordService.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMobileNumber(request.getMobileNumber());
        user.setRecoveryEmail(request.getRecoveryEmail());

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        Optional<User> byUsername = userRepository.findByUsername(loginId);
        if (byUsername.isPresent()) {
            return byUsername;
        }
        return userRepository.findByEmail(loginId);
    }

}
