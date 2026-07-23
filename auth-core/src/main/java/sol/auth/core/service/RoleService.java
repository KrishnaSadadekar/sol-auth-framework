package sol.auth.core.service;

import java.util.List;
import java.util.Optional;

import sol.auth.core.entity.Role;

public interface RoleService {

    Role createRole(Role role);

    Role updateRole(Long roleId, Role role);

    void deleteRole(Long roleId);

    Optional<Role> findById(Long roleId);

    Optional<Role> findByName(String roleName);

    List<Role> findAllRoles();

}
