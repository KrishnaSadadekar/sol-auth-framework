package sol.auth.core.service;

import java.util.List;
import java.util.Optional;

import sol.auth.core.entity.Permission;

public interface PermissionService {

    Permission createPermission(Permission permission);

    Permission updatePermission(Long permissionId, Permission permission);

    void deletePermission(Long permissionId);

    Optional<Permission> findById(Long permissionId);

    Optional<Permission> findByName(String permissionName);

    List<Permission> findAllPermissions();

}
