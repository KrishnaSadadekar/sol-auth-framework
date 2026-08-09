package sol.auth.core.event;

import sol.auth.core.entity.User;

public record UserLoggedOutEvent(User user) {
}
