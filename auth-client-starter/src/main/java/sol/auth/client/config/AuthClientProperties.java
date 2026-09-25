package sol.auth.client.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "auth.client")
public class AuthClientProperties {

    private boolean enabled = true;
    private String serverUrl = "http://localhost:8080";
    private String secret = "my-local-development-secret-key-at-least-32-bytes";
    private String issuer = "auth-demo";
    private List<String> permitAll = new ArrayList<>(List.of(
            "/public/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/actuator/**"
    ));
}
