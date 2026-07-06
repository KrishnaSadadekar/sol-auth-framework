package sol.auth.core.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    
private String username ;
private String email ;
private String password ;
private String firstName ;
private String lastName ;
private String mobileNumber ;
private String profileImage ;
private Boolean  emailVerified ;
private Boolean mobileVerified ;
private Boolean accountLocked ; // For Temporary Locking of the account after multiple failed login attempts
private Boolean accountExpired ;
private Boolean credentialsExpired ;
private Boolean enabled ;
private String lastLogin ;
private String recoveryEmail;
private String failedLoginAttempts ;
    
}
