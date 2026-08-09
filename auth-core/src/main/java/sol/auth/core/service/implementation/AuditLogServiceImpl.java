package sol.auth.core.service.implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import sol.auth.common.enums.AuditAction;
import sol.auth.core.entity.AuditLog;
import sol.auth.core.event.UserLockedEvent;
import sol.auth.core.event.UserLoggedInEvent;
import sol.auth.core.event.UserLoggedOutEvent;
import sol.auth.core.event.UserRegisteredEvent;
import sol.auth.core.repository.AuditLogRepository;
import sol.auth.core.service.AuditLogService;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Async
    @EventListener
    @Override
    public void onUserRegistered(UserRegisteredEvent event) {
        AuditLog log = AuditLog.builder()
                .user(event.user())
                .action(AuditAction.USER_CREATED)
                .description("User registered: " + event.user().getUsername())
                .ipAddress(event.ipAddress())
                .userAgent(event.userAgent())
                .success(true)
                .actionTime(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }

    @Async
    @EventListener
    @Override
    public void onUserLoggedIn(UserLoggedInEvent event) {
        AuditLog log = AuditLog.builder()
                .user(event.user())
                .action(AuditAction.LOGIN)
                .description("User logged in: " + event.user().getUsername())
                .ipAddress(event.ipAddress())
                .userAgent(event.userAgent())
                .success(true)
                .actionTime(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }

    @Async
    @EventListener
    @Override
    public void onUserLocked(UserLockedEvent event) {
        AuditLog log = AuditLog.builder()
                .user(event.user())
                .action(AuditAction.ACCOUNT_LOCKED)
                .description("Account locked due to excessive failed login attempts: " + event.user().getUsername())
                .success(true)
                .actionTime(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }

    @Async
    @EventListener
    @Override
    public void onUserLoggedOut(UserLoggedOutEvent event) {
        AuditLog log = AuditLog.builder()
                .user(event.user())
                .action(AuditAction.LOGOUT)
                .description("User logged out: " + event.user().getUsername())
                .success(true)
                .actionTime(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }

    @Override
    public AuditLog save(AuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }

    @Override
    public List<AuditLog> findByUserId(Long userId) {
        return auditLogRepository.findByUser_Id(userId);
    }
}
