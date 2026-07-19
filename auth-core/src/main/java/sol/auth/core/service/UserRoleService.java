package sol.auth.core.service;

import java.util.List;

import sol.auth.core.entity.Role;
import sol.auth.core.entity.User;
import sol.auth.core.entity.UserRole;

public interface UserRoleService {

    
    UserRole assignRole(User user, Role role);    

    void removeRole(Long userId, Long roleId);

    List<Role> getRolesByUser(Long userId);
}
