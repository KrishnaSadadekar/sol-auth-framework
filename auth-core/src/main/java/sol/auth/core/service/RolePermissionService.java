package sol.auth.core.service;

import java.util.List;

import sol.auth.core.entity.Permission;

public interface RolePermissionService {

    void assignPermission(Long roleId, Long permissionId);

    void removePermission(Long roleId, Long permissionId);

    List<Permission> getPermissionsByRole(Long roleId);

    boolean hasPermission(Long roleId, String permissionName);

}