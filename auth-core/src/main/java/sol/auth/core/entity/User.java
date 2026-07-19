package sol.auth.core.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    private Boolean emailVerified = false;

    private Boolean mobileVerified = false;

    private Boolean accountLocked = false;

    private LocalDateTime accountLockedAt;

    private Boolean accountExpired = false;

    private Boolean credentialsExpired = false;

    private LocalDateTime lastLogin;

    public void setEnabled(boolean b) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEnabled'");
    }

    public void setFailedLoginAttempts(int i) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFailedLoginAttempts'");
    }

}
