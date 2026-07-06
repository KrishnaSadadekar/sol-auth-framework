package sol.auth.core.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "login_attempts")
public class LoginAttempt extends BaseEntity {

    private User user;

    private String username;

    private String ipAddress;

    private String userAgent;

    private Boolean successful;

    private String failureReason;

    private LocalDateTime loginTime;

}
