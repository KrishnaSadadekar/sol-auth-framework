package sol.auth.client.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import sol.auth.client.config.AuthClientProperties;
import sol.auth.client.jwt.JwtValidator;
import sol.auth.client.sdk.UserManagementClient;
import sol.auth.client.security.JwtAuthenticationFilter;
import sol.auth.client.tenant.TenantFilter;

@AutoConfiguration(before = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
@ConditionalOnWebApplication
@EnableConfigurationProperties(AuthClientProperties.class)
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AuthClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtValidator jwtValidator(AuthClientProperties properties) {
        return new JwtValidator(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtValidator jwtValidator) {
        return new JwtAuthenticationFilter(jwtValidator);
    }

    @Bean
    @ConditionalOnMissingBean
    public TenantFilter tenantFilter() {
        return new TenantFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserManagementClient userManagementClient(AuthClientProperties properties, ObjectMapper objectMapper) {
        return new UserManagementClient(properties, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException("Resource Server authenticates via JWT Bearer tokens only");
        };
    }

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain resourceServerSecurityFilterChain(
            HttpSecurity http,
            AuthClientProperties properties,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            TenantFilter tenantFilter) throws Exception {

        String[] permitAllPatterns = properties.getPermitAll().toArray(new String[0]);

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(permitAllPatterns).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(tenantFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
