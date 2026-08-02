package sol.auth.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sol.auth.core.entity.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByTenantCode(String tenantCode);

    boolean existsByTenantCode(String tenantCode);
}
