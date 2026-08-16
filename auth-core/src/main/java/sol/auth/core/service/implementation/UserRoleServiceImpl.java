package sol.auth.core.service.implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sol.auth.core.entity.Role;
import sol.auth.core.entity.User;
import sol.auth.core.entity.UserRole;
import sol.auth.core.repository.UserRoleRepository;
import sol.auth.core.service.UserRoleService;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    @Override
    public void removeRole(Long userId, Long roleId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeRole'");
    }

    @Override
    public List<Role> getRolesByUser(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRolesByUser'");
    }

    @Override
    public UserRole assignRole(User user, Role role) {
        UserRole userRole = new UserRole();

        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setPrimaryRole(true);
        userRole.setAssignedAt(LocalDateTime.now());
        userRole.setCreatedAt(LocalDateTime.now());
        userRole.setCreatedBy("CA");
        userRole.setUpdatedAt(LocalDateTime.now());
        userRole.setUpdatedBy("CA");
        System.out.println("User Role " + userRole);
        return userRoleRepository.save(userRole);
    }

}
