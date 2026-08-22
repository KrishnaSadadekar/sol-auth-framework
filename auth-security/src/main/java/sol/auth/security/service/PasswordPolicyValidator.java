package sol.auth.security.service;

import org.springframework.stereotype.Component;
import sol.auth.security.config.PasswordPolicy;

import java.util.regex.Pattern;

@Component("passwordPolicyValidator")
public class PasswordPolicyValidator {

        private static final Pattern UPPERCASE_PATTERN = Pattern.compile(".*[A-Z].*");
        private static final Pattern LOWERCASE_PATTERN = Pattern.compile(".*[a-z].*");
        private static final Pattern DIGIT_PATTERN = Pattern.compile(".*\\d.*");
        private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(".*[^a-zA-Z0-9].*");

        private final PasswordPolicy policy;

        public PasswordPolicyValidator(PasswordPolicy policy) {
                this.policy = policy;
        }

        public void validate(String password) {
                if (password == null || password.isBlank()) {
                        throw new IllegalArgumentException("Password cannot be blank");
                }

                if (password.length() < policy.getMinimumLength()) {
                        throw new IllegalArgumentException(
                                        "Password must contain at least " + policy.getMinimumLength() + " characters");
                }

                if (password.length() > policy.getMaximumLength()) {
                        throw new IllegalArgumentException(
                                        "Password cannot exceed " + policy.getMaximumLength() + " characters");
                }

                if (policy.isRequireUppercase() && !UPPERCASE_PATTERN.matcher(password).matches()) {
                        throw new IllegalArgumentException("Password must contain an uppercase letter");
                }

                if (policy.isRequireLowercase() && !LOWERCASE_PATTERN.matcher(password).matches()) {
                        throw new IllegalArgumentException("Password must contain a lowercase letter");
                }

                if (policy.isRequireDigit() && !DIGIT_PATTERN.matcher(password).matches()) {
                        throw new IllegalArgumentException("Password must contain a digit");
                }

                if (policy.isRequireSpecialCharacter() && !SPECIAL_CHAR_PATTERN.matcher(password).matches()) {
                        throw new IllegalArgumentException("Password must contain a special character");
                }
        }
}