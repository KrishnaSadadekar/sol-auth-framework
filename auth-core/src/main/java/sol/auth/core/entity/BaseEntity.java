package sol.auth.core.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @CreatedBy
  @Column(nullable = false)
  private String createdBy;

  @LastModifiedBy
  @Column(nullable = false)
  private String updatedBy;

  @Column(nullable = false)
  private Boolean active = true;

  @Column(nullable = false)
  private Boolean deleted = false;

  @Column(name = "tenant_id")
  private Long tenantId;
}