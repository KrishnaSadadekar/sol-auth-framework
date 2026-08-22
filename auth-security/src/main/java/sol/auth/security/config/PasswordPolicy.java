package sol.auth.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "app.security.password-policy")
@Getter
@Setter
public class PasswordPolicy {
    private int minimumLength = 8;
    private int maximumLength = 128;

    private boolean requireUppercase = true;
    private boolean requireLowercase = true;
    private boolean requireDigit = true;
    private boolean requireSpecialCharacter = true;
}
