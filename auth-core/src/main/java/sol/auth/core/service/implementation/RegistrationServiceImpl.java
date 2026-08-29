package sol.auth.core.service.implementation;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import sol.auth.core.dto.RegisterRequest;
import sol.auth.core.entity.Role;
import sol.auth.core.entity.User;
import sol.auth.core.event.UserRegisteredEvent;
import sol.auth.core.exception.UserAlreadyExistsException;
import sol.auth.core.repository.UserRepository;
import sol.auth.core.service.PasswordService;
import sol.auth.core.service.RegistrationService;
import sol.auth.core.service.RoleService;
import sol.auth.core.service.UserRoleService;
import sol.auth.core.service.UserService;
import sol.auth.core.tenant.TenantContext;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final ApplicationEventPublisher eventPublisher;

    public RegistrationServiceImpl(UserService userService,
            UserRepository userRepository,
            PasswordService passwordService,
            RoleService roleService,
            UserRoleService userRoleService,
            ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.roleService = roleService;
        this.userRoleService = userRoleService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public User register(RegisterRequest request) {
        validate(request);
        User user = buildUser(request);
        user = userService.create(user);
        assignDefaultRole(user);
        eventPublisher.publishEvent(new UserRegisteredEvent(user, null, null));
        return user;
    }

    private void validate(RegisterRequest request) {
        Long tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            if (userRepository.existsByUsernameAndTenantId(request.getUsername(), tenantId)) {
                throw new UserAlreadyExistsException("Username already exists in this tenant");
            }
            if (userRepository.existsByEmailAndTenantId(request.getEmail(), tenantId)) {
                throw new UserAlreadyExistsException("Email already exists in this tenant");
            }
        } else {
            userService.findByUsername(request.getUsername()).ifPresent(u -> {
                throw new UserAlreadyExistsException("Username already exists");
            });
            userService.findByEmail(request.getEmail()).ifPresent(u -> {
                throw new UserAlreadyExistsException("Email already exists");
            });
        }
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
        user.setTenantId(TenantContext.getTenantId());
        return user;
    }

    private void assignDefaultRole(User user) {
        Role defaultRole = roleService
                .findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        userRoleService.assignRole(user, defaultRole);
    }

}
