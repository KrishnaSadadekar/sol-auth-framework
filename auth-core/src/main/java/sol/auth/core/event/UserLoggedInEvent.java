package sol.auth.core.event;

import sol.auth.core.entity.User;

public record UserLoggedInEvent(User user, String ipAddress, String userAgent) {
}
