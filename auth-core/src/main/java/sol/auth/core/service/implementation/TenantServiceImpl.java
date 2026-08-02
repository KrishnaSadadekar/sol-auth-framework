package sol.auth.core.service.implementation;

import java.util.Optional;

import org.springframework.stereotype.Service;

import sol.auth.core.entity.Tenant;
import sol.auth.core.exception.TenantResolutionException;
import sol.auth.core.repository.TenantRepository;
import sol.auth.core.service.TenantService;

@Service
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;

    public TenantServiceImpl(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public Optional<Tenant> findByCode(String tenantCode) {
        return tenantRepository.findByTenantCode(tenantCode);
    }

    @Override
    public Tenant getByCode(String tenantCode) {
        return tenantRepository.findByTenantCode(tenantCode)
                .filter(t -> Boolean.TRUE.equals(t.getEnabled())
                        && Boolean.TRUE.equals(t.getActive())
                        && !Boolean.TRUE.equals(t.getDeleted()))
                .orElseThrow(() -> new TenantResolutionException(
                        "Tenant not found or inactive: " + tenantCode));
    }

    @Override
    public Tenant getById(Long tenantId) {
        return tenantRepository.findById(tenantId)
                .filter(t -> Boolean.TRUE.equals(t.getEnabled())
                        && Boolean.TRUE.equals(t.getActive())
                        && !Boolean.TRUE.equals(t.getDeleted()))
                .orElseThrow(() -> new TenantResolutionException(
                        "Tenant not found: " + tenantId));
    }

    @Override
    public boolean exists(String tenantCode) {
        return tenantRepository.existsByTenantCode(tenantCode);
    }
}
