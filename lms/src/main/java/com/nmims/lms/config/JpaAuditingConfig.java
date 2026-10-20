package com.nmims.lms.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import com.nmims.lms.context.UserContextHolder;

@Configuration
public class JpaAuditingConfig {

    @Bean(name = "auditorAware")
    public AuditorAware<String> auditorAware() {
        return () -> {
            String username = UserContextHolder.get();
            if (username != null && !username.isBlank()) {
                return Optional.of(username);
            }
            return Optional.of("system");
        };
    }
}
