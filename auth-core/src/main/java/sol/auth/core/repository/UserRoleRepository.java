package sol.auth.core.repository;

import java.util.List;

import javax.management.relation.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sol.auth.core.entity.RolePermission;
import sol.auth.core.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
      List<RolePermission> findByRole(Role role);

      List<UserRole> findByUser(sol.auth.core.entity.User user);

      @Query("""
                      SELECT ur
                      FROM UserRole ur
                      JOIN FETCH ur.role
                      WHERE ur.user.id = :userId
                  """)
      List<UserRole> findRolesByUserId(@Param("userId") Long userId);
}
