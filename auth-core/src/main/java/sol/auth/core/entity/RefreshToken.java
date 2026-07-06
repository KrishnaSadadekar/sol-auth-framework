package sol.auth.core.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class RefreshToken extends BaseEntity {
    private String token;
    private Long userId;
    
}
