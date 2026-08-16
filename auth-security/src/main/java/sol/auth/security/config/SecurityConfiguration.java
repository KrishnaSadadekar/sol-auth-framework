package sol.auth.security.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import sol.auth.security.filter.JwtAuthenticationFilter;
import sol.auth.security.provider.CustomAuthenticationProvider;

@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {

        @Value("${auth.cors.allowed-origins:*}")
        private String allowedOrigins;

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final CustomAuthenticationProvider customAuthenticationProvider;

        public SecurityConfiguration(
                        JwtAuthenticationFilter jwtAuthenticationFilter,
                        CustomAuthenticationProvider customAuthenticationProvider) {

                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
                this.customAuthenticationProvider = customAuthenticationProvider;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())

                                .cors(cors -> cors
                                                .configurationSource(corsConfigurationSource()))

                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(
                                                                SessionCreationPolicy.STATELESS))

                                .authenticationProvider(
                                                customAuthenticationProvider)

                                .authorizeHttpRequests(auth -> auth

                                                .requestMatchers(
                                                                "/api/v1/auth/login",
                                                                "/api/v1/auth/register",
                                                                "/api/v1/auth/refresh")
                                                .permitAll()

                                                .requestMatchers(
                                                                "/api/v1/auth/me",
                                                                "/api/v1/auth/logout")
                                                .authenticated()

                                                .anyRequest().authenticated())

                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

                CorsConfiguration config = new CorsConfiguration();

                List<String> origins = Arrays.asList(allowedOrigins.split(","));

                config.setAllowedOriginPatterns(origins);

                config.setAllowedMethods(Arrays.asList(
                                "GET",
                                "POST",
                                "PUT",
                                "PATCH",
                                "DELETE",
                                "OPTIONS"));

                config.setAllowedHeaders(Arrays.asList(
                                "Authorization",
                                "Content-Type",
                                "X-Tenant-Id",
                                "X-Correlation-Id"));

                config.setExposedHeaders(
                                List.of("X-Correlation-Id"));

                config.setAllowCredentials(true);
                config.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration(
                                "/**",
                                config);

                return source;
        }
}