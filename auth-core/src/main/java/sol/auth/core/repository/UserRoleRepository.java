package sol.auth.core.repository;

import java.util.List;

import javax.management.relation.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import sol.auth.core.entity.RolePermission;
import sol.auth.core.entity.UserRole;

public interface UserRoleRepository  extends JpaRepository<UserRole, Long> { 
      List<RolePermission> findByRole(Role role);

      List<UserRole> findByUser(sol.auth.core.entity.User user);
}
