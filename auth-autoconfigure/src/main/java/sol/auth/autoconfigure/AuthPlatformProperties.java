package sol.auth.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth")
public class AuthPlatformProperties {

    private final Security security = new Security();
    private final Jwt jwt = new Jwt();

    public Security getSecurity() {
        return security;
    }

    public Jwt getJwt() {
        return jwt;
    }

    public static class Security {
        private int maxFailedLoginAttempts = 5;
        private long lockoutDurationMinutes = 30;
        private boolean requireEmailVerification = false;

        public int getMaxFailedLoginAttempts() {
            return maxFailedLoginAttempts;
        }

        public void setMaxFailedLoginAttempts(int v) {
            this.maxFailedLoginAttempts = v;
        }

        public long getLockoutDurationMinutes() {
            return lockoutDurationMinutes;
        }

        public void setLockoutDurationMinutes(long v) {
            this.lockoutDurationMinutes = v;
        }

        public boolean isRequireEmailVerification() {
            return requireEmailVerification;
        }

        public void setRequireEmailVerification(boolean v) {
            this.requireEmailVerification = v;
        }
    }

    public static class Jwt {
        private String secret = "change-this-secret-key-to-a-32-byte-minimum-value";
        private String issuer = "auth-platform";
        private long accessTokenTtlSeconds = 900;
        private long refreshTokenTtlSeconds = 604800;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String v) {
            this.secret = v;
        }

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String v) {
            this.issuer = v;
        }

        public long getAccessTokenTtlSeconds() {
            return accessTokenTtlSeconds;
        }

        public void setAccessTokenTtlSeconds(long v) {
            this.accessTokenTtlSeconds = v;
        }

        public long getRefreshTokenTtlSeconds() {
            return refreshTokenTtlSeconds;
        }

        public void setRefreshTokenTtlSeconds(long v) {
            this.refreshTokenTtlSeconds = v;
        }
    }
}
