package sol.auth.service.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sol.auth.core.entity.Tenant;
import sol.auth.core.exception.TenantResolutionException;
import sol.auth.core.service.TenantService;
import sol.auth.core.tenant.TenantContext;

@Component
@Order(1)
public class TenantFilter extends OncePerRequestFilter {

    static final String TENANT_HEADER = "X-Tenant-Id";

    private final TenantService tenantService;

    public TenantFilter(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String tenantCode = request.getHeader(TENANT_HEADER);

        try {
            if (tenantCode != null && !tenantCode.isBlank()) {
                Tenant tenant = tenantService.getByCode(tenantCode.trim());
                TenantContext.setTenantId(tenant.getId());
            }
            filterChain.doFilter(request, response);
        } catch (TenantResolutionException ex) {
            TenantContext.clear();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"code\":\"TENANT_NOT_FOUND\",\"message\":\"" + ex.getMessage() + "\"}");
        } finally {
            TenantContext.clear();
        }
    }
}
