package sol.auth.core.dto;

import lombok.Getter;
import lombok.Setter;
import sol.auth.core.entity.User;

@Getter
@Setter
public class LoginAttemptRequest {
    private User user;

    private String username;

    private String ipAddress;

    private String userAgent;

    private Boolean success;

    private String failureReason;

}
