package sol.auth.core.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import sol.auth.common.enums.AuditAction;
@Data
@Entity
@Table(name = "audit_logs")
public class AuditLog extends BaseEntity {

    private User user;

    /**
     * LOGIN, LOGOUT, PASSWORD_CHANGED, etc.
     */
    @Enumerated(EnumType.STRING)
    private AuditAction action;

    /**
     * Description of the action.
     */
    private String description;

    /**
     * IP Address of the client.
     */
    private String ipAddress;

    /**
     * Browser / Client information.
     */
    private String userAgent;

    /**
     * SUCCESS / FAILED
     */
    private Boolean success;

    /**
     * When the event occurred.
     */
    private LocalDateTime actionTime;
}
