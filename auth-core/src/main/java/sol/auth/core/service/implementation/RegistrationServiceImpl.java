package sol.auth.core.service.implementation;

import org.springframework.transaction.annotation.Transactional;

import sol.auth.core.dto.RegisterRequest;
import sol.auth.core.entity.Role;
import sol.auth.core.entity.User;
import sol.auth.core.service.PasswordService;
import sol.auth.core.service.RegistrationService;
import sol.auth.core.service.RoleService;
import sol.auth.core.service.UserRoleService;
import sol.auth.core.service.UserService;

public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;
    private final PasswordService passwordService;
    private final RoleService roleService;
    private final UserRoleService userRoleService;

    public RegistrationServiceImpl(UserService userService, PasswordService passwordService, RoleService roleService,
            UserRoleService userRoleService) {
        this.userService = userService;
        this.passwordService = passwordService;
        this.roleService = roleService;
        this.userRoleService = userRoleService;
    }

    @Override
    @Transactional
    public User register(RegisterRequest request) {
        validate(request);
        User user = buildUser(request);
        user = userService.create(user);
        assignDefaultRole(user);
        return user;
    }


     private void validate(RegisterRequest request) {
        userService.findByUsername(request.getUsername()).ifPresent(u -> {
            throw new RuntimeException("Username already exists");
        });
        userService.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email already exists");
        });
    }

    private User buildUser(RegisterRequest request) {
         User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordService.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .mobileNumber(request.getMobileNumber())
                .recoveryEmail(request.getRecoveryEmail())
                               
                .build();

                 user.setActive(true);
                 user.setEnabled(true);
                 user.setDeleted(false);
                 user.setEmailVerified(false);
                 user.setMobileVerified(false);
                 user.setAccountLocked(false);
                 user.setAccountExpired(false);
                 user.setCredentialsExpired(false);
                 user.setFailedLoginAttempts(0);
              
        return user;
    }

    private void assignDefaultRole(User user) {
       Role defaultRole = roleService
            .findByName("ROLE_USER")
            .orElseThrow(() ->
                    new RuntimeException("Default role not found"));
       userRoleService.assignRole(user, defaultRole);
    }

}
