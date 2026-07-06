package sol.auth.core.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "password_history")
public class PasswordHistory extends BaseEntity {

    private User user;

    private String passwordHash;

    private LocalDateTime changedAt;

}
