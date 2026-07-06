package sol.auth.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "permissions")
public class Permission extends BaseEntity {

    private String permissionCode;
    private String permissionName;
    private String description;
    private String module;
}
