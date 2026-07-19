package sol.auth.core.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sol.auth.core.entity.Role;
import sol.auth.core.exception.RoleAlreadyExistsException;
import sol.auth.core.exception.RoleNotFoundException;
import sol.auth.core.repository.RoleRepository;
import sol.auth.core.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(Role role) {

        if (roleRepository.findByRoleName(role.getRoleName()).isPresent()) {
            throw new RoleAlreadyExistsException(
                    "Role already exists : " + role.getRoleName());
        }

        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Long roleId, Role role) {

        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        existingRole.setRoleName(role.getRoleName());
        existingRole.setDescription(role.getDescription());

        return roleRepository.save(existingRole);
    }

    @Override
    public void deleteRole(Long roleId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        roleRepository.delete(role);
    }

    @Override
    public Optional<Role> findById(Long roleId) {
        return roleRepository.findById(roleId);
    }

    @Override
    public Optional<Role> findByName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
}