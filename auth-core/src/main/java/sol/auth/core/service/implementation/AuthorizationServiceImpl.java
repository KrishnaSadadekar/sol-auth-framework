package sol.auth.core.service.implementation;

import java.util.Set;
import java.util.stream.Collectors;

import sol.auth.core.entity.Permission;
import sol.auth.core.entity.Role;
import sol.auth.core.entity.RolePermission;
import sol.auth.core.entity.User;
import sol.auth.core.entity.UserRole;
import sol.auth.core.repository.RolePermissionRepository;
import sol.auth.core.repository.UserRoleRepository;
import sol.auth.core.service.AuthorizationService;

public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public AuthorizationServiceImpl(UserRoleRepository userRoleRepository,
                                    RolePermissionRepository rolePermissionRepository) {
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public boolean hasRole(User user, String roleName) {
         return getRoles(user)
            .stream()
            .anyMatch(role ->
                    role.getRoleName().equalsIgnoreCase(roleName));
    }

    @Override
    public boolean hasPermission(User user, String permissionName) {
           return getPermissions(user)
            .stream()
            .anyMatch(permission ->
                    permission.getPermissionName()
                            .equalsIgnoreCase(permissionName));
    }

    @Override
    public Set<Role> getRoles(User user) {
      
       return  userRoleRepository.findByUser(user)
            .stream()
            .map(UserRole::getRole)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<Permission> getPermissions(User user) {
       
       return getRoles(user)
            .stream()
            .flatMap(role ->
                    rolePermissionRepository.findByRole(role)
                            .stream())
            .map(RolePermission::getPermission)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getRoleNames(User user) {
        return getRoles(user)
            .stream()
            .map(Role::getRoleName)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getPermissionNames(User user) {
        return getPermissions(user)
            .stream()
            .map(Permission::getPermissionName)
            .collect(Collectors.toSet());
    }

  
}
