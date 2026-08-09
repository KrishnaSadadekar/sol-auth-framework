package sol.auth.core.service;

import sol.auth.core.entity.AuditLog;
import sol.auth.core.event.UserLockedEvent;
import sol.auth.core.event.UserLoggedInEvent;
import sol.auth.core.event.UserLoggedOutEvent;
import sol.auth.core.event.UserRegisteredEvent;

import java.util.List;

public interface AuditLogService {

    void onUserRegistered(UserRegisteredEvent event);

    void onUserLoggedIn(UserLoggedInEvent event);

    void onUserLocked(UserLockedEvent event);

    void onUserLoggedOut(UserLoggedOutEvent event);

    AuditLog save(AuditLog auditLog);

    List<AuditLog> findByUserId(Long userId);
}
