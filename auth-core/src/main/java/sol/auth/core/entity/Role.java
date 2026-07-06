package sol.auth.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

private String roleName;
private String description;
private Boolean isSystemRole;

}
