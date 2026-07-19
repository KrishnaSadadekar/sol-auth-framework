package sol.auth.core.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sol.auth.core.entity.Permission;

public interface PermissionRepository  extends JpaRepository<Permission, Long> {
    
    Optional<Permission> findByPermissionName(String permissionName);
}
