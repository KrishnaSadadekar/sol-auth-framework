package sol.auth.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sol.auth.core.entity.Role;
import sol.auth.core.entity.RolePermission;

public interface RolePermissionRepository  extends JpaRepository<RolePermission, Long>  {
    List<RolePermission> findByRole(Role role);
}
