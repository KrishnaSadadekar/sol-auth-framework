package sol.auth.core.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken extends BaseEntity {

    private String token;

    private Long userId;

    private LocalDateTime expiresAt;

    @Default
    private Boolean revoked = false;

}
