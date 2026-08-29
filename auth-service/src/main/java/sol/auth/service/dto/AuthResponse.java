package sol.auth.service.dto;

public class AuthResponse {

    private AuthTokenResponse token;
    private UserSummaryResponse user;

    private String resetEmailLink;

    public AuthTokenResponse getToken() {
        return token;
    }

    public void setToken(AuthTokenResponse token) {
        this.token = token;
    }

    public UserSummaryResponse getUser() {
        return user;
    }

    public void setUser(UserSummaryResponse user) {
        this.user = user;
    }

    public String getResetEmailLink() {
        return resetEmailLink;
    }

    public void setResetEmailLink(String resetEmailLink) {
        this.resetEmailLink = resetEmailLink;
    }

}
