package sol.auth.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import sol.auth.core.service.PasswordService;
import sol.auth.security.config.AuthenticationManagerConfiguration;
import sol.auth.security.config.PasswordPolicy;
import sol.auth.security.config.SecurityConfiguration;
import sol.auth.security.config.SpringSecurityAuditorAware;
import sol.auth.security.service.PasswordPolicyValidator;
import sol.auth.security.service.Implementation.BCryptPasswordService;

@AutoConfiguration(before = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
@EnableConfigurationProperties(PasswordPolicy.class)
@Import({
                SecurityConfiguration.class,
                AuthenticationManagerConfiguration.class,
                SpringSecurityAuditorAware.class,
                PasswordPolicyValidator.class
})

public class AuthSecurityAutoConfiguration {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        @ConditionalOnMissingBean
        public PasswordService passwordService(PasswordEncoder passwordEncoder,
                        PasswordPolicyValidator passwordPolicyValidator) {
                return new BCryptPasswordService(passwordEncoder, passwordPolicyValidator);
        }

}