package com.nmims.lms.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sol.auth.client.principal.AuthUserPrincipal;

/**
 * Extracts the authenticated user from Spring Security's SecurityContext
 * (populated by auth-client-starter from JWT), with fallback to X-User header.
 */
@Component
public class UserContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AuthUserPrincipal principal) {
            UserContextHolder.set(principal.getUsername());
        } else {
            String username = request.getHeader("X-User");
            if (username != null && !username.isBlank()) {
                UserContextHolder.set(username);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        UserContextHolder.clear();
    }
}
