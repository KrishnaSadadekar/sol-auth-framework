package sol.auth.core.service;

import java.util.Optional;

import sol.auth.core.entity.Tenant;

public interface TenantService {

    Optional<Tenant> findByCode(String tenantCode);

    Tenant getByCode(String tenantCode);

    Tenant getById(Long tenantId);

    boolean exists(String tenantCode);
}
