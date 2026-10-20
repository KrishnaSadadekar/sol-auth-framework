package com.nmims.lms.context;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import sol.auth.client.principal.AuthUserPrincipal;
import sol.auth.client.tenant.TenantContext;

/**
 * Bridges authenticated identity from Spring Security's AuthUserPrincipal
 * (populated by auth-client-starter from JWT) with backward-compatible ThreadLocal support.
 */
public final class UserContextHolder {

    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();

    private UserContextHolder() {}

    public static void set(String username) {
        USERNAME.set(username);
    }

    public static String get() {
        Optional<AuthUserPrincipal> principal = getPrincipal();
        if (principal.isPresent()) {
            return principal.get().getUsername();
        }
        return USERNAME.get();
    }

    public static void clear() {
        USERNAME.remove();
    }

    public static Optional<AuthUserPrincipal> getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AuthUserPrincipal principal) {
            return Optional.of(principal);
        }
        return Optional.empty();
    }

    public static Long getUserId() {
        return getPrincipal().map(AuthUserPrincipal::getUserId).orElse(null);
    }

    public static Long getTenantId() {
        return getPrincipal()
                .map(AuthUserPrincipal::getTenantId)
                .orElseGet(() -> TenantContext.hasTenant() ? TenantContext.getTenantId() : null);
    }

    public static List<String> getRoles() {
        return getPrincipal().map(AuthUserPrincipal::getRoles).orElse(Collections.emptyList());
    }

    public static List<String> getPermissions() {
        return getPrincipal().map(AuthUserPrincipal::getPermissions).orElse(Collections.emptyList());
    }

    public static boolean hasRole(String role) {
        if (role == null) return false;
        String normalized = role.startsWith("ROLE_") ? role.substring(5) : role;
        return getRoles().stream().anyMatch(r -> {
            String rNorm = r.startsWith("ROLE_") ? r.substring(5) : r;
            return rNorm.equalsIgnoreCase(normalized);
        });
    }

    public static boolean hasPermission(String permission) {
        if (permission == null) return false;
        return getPermissions().stream().anyMatch(p -> p.equalsIgnoreCase(permission));
    }
}
