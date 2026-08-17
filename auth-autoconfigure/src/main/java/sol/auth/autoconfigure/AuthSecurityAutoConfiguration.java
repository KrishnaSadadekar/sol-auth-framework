package sol.auth.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.annotation.PostConstruct;
import sol.auth.security.config.AuthenticationManagerConfiguration;
import sol.auth.security.config.SecurityConfiguration;
import sol.auth.security.config.SpringSecurityAuditorAware;

@AutoConfiguration(before = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
@Import({
                SecurityConfiguration.class,
                AuthenticationManagerConfiguration.class,
                SpringSecurityAuditorAware.class
})

public class AuthSecurityAutoConfiguration {

    

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}