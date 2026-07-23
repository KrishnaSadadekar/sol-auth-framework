package sol.auth.service.dto;

public class AuthResponse {

    private AuthTokenResponse token;
    private UserSummaryResponse user;

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
}
