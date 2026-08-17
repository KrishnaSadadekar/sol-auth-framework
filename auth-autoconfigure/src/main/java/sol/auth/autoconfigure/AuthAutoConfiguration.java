package sol.auth.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;


import sol.auth.jwt.configuration.JwtTokenProperties;

@AutoConfiguration
@EnableConfigurationProperties({
                AuthPlatformProperties.class,
                JwtTokenProperties.class
})
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableJpaRepositories(basePackages = "sol.auth.core.repository")
@EntityScan(basePackages = "sol.auth.core.entity")
@EnableAsync
@ComponentScan(basePackages = {
                "sol.auth.core.configuration",
                "sol.auth.core.service.implementation",
                "sol.auth.security.filter",
                "sol.auth.security.provider",
                "sol.auth.security.service",
                "sol.auth.jwt.service.implementation",
                "sol.auth.service"

})
public class AuthAutoConfiguration {

       
}