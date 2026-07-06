package sol.auth.core.entity;

import java.time.LocalDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;


@Data
@MappedSuperclass
public abstract class BaseEntity {
      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime  createdAt;
    private LocalDateTime  updatedAt;
    private String createdBy;
    private String updatedBy;
    private Boolean active;
    private Boolean deleted;
    private String remarks;

}
