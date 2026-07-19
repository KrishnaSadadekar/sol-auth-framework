package sol.auth.core.service;

import java.util.Set;

import sol.auth.core.entity.Permission;
import sol.auth.core.entity.Role;
import sol.auth.core.entity.User;

public interface AuthorizationService {

    boolean hasRole(User user, String roleName);

    boolean hasPermission(User user, String permissionName);

    Set<Role> getRoles(User user);

    Set<Permission> getPermissions(User user);

    Set<String> getRoleNames(User user);

    Set<String> getPermissionNames(User user);

}