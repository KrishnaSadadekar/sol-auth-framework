package sol.auth.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sol.auth.core.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
