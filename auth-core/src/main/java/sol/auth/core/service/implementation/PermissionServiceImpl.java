package sol.auth.core.service.implementation;

import java.util.List;
import java.util.Optional;

import sol.auth.core.entity.Permission;
import sol.auth.core.exception.PermissionAlreadyExistsException;
import sol.auth.core.exception.PermissionNotFoundException;
import sol.auth.core.repository.PermissionRepository;
import sol.auth.core.service.PermissionService;

public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission createPermission(Permission permission) {

        if (permissionRepository
                .findByPermissionName(permission.getPermissionName())
                .isPresent()) {

            throw new PermissionAlreadyExistsException(
                    "Permission already exists");
        }

        return permissionRepository.save(permission);
    }

    @Override
    public Permission updatePermission(Long permissionId,
            Permission permission) {

        Permission existingPermission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new PermissionNotFoundException(
                        "Permission not found"));

        existingPermission.setPermissionName(permission.getPermissionName());
        existingPermission.setDescription(permission.getDescription());

        return permissionRepository.save(existingPermission);
    }

    @Override
    public void deletePermission(Long permissionId) {

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new PermissionNotFoundException(
                        "Permission not found"));

        permission.setDeleted(true);
        permission.setActive(false);

        permissionRepository.save(permission);
    }

    @Override
    public Optional<Permission> findById(Long permissionId) {
        return permissionRepository.findById(permissionId);
    }

    @Override
    public Optional<Permission> findByName(String permissionName) {
        return permissionRepository.findByPermissionName(permissionName);
    }

    @Override
    public List<Permission> findAllPermissions() {
        return permissionRepository.findAll();
    }

}
