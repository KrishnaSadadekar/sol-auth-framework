package sol.auth.core.event;

import sol.auth.core.entity.User;

/**
 * UserPasswordChangedEvent
 */
public record UserPasswordChangedEvent(User user, String ipAddress, String userAgent) {

}
