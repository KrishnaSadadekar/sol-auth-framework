package sol.auth.core.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;
import jakarta.persistence.Index;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_email", columnList = "email")
})
public class User extends BaseEntity {

    private String username;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String mobileNumber;

    private String recoveryEmail;

    private String profileImage;

    @Default
    private Boolean emailVerified = false;

    @Default
    private Boolean mobileVerified = false;

    @Default
    private Boolean accountLocked = false;

    private LocalDateTime accountLockedAt;

    @Default
    private Boolean accountExpired = false;

    @Default
    private Boolean credentialsExpired = false;

    @Default
    private Boolean enabled = true;

    @Default
    private Integer failedLoginAttempts = 0;

    private LocalDateTime lastLogin;

    public void setEnabled(boolean b) {
        this.enabled = b;
    }

    public void setFailedLoginAttempts(int i) {
        this.failedLoginAttempts = i;
    }

}
