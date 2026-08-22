package sol.auth.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sol.auth.core.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String roleName);

    @Query("""
                SELECT ur
                FROM UserRole ur
                JOIN FETCH ur.role
                WHERE ur.user.id = :userId
            """)
    List<Role> findRolesByUserId(Long userId);
}
