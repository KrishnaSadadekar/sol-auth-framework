package sol.auth.core.event;

import sol.auth.core.entity.User;

public record UserLockedEvent(User user) {
}
