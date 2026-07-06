package sol.auth.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user_sessions")
public class UserSession extends BaseEntity {
    private String sessionId;
    private Long userId;

    
}
