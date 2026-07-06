package sol.auth.core.repository;

import java.security.Permission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository  extends JpaRepository<Permission, Long> {
    
}
