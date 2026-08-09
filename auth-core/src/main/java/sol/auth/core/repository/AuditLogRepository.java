package sol.auth.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sol.auth.common.enums.AuditAction;
import sol.auth.core.entity.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUser_Id(Long userId);

    List<AuditLog> findByAction(AuditAction action);

    List<AuditLog> findByTenantId(Long tenantId);
}
