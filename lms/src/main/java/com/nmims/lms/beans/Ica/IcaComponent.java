package com.nmims.lms.beans.Ica;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "ica_components")
  public class IcaComponent extends BaseBean {

    // Foreign key to pre_def_ica_components(id)
    @Column(name = "componentId", nullable = false)
    private Long componentId;

    // Foreign key to ica(id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icaId")
    @JsonIgnore
    private Ica ica;

    private Double marks;

    @Column(length = 50, columnDefinition = "varchar(50) default 'Y'")
    private String active;

    @Column(length = 50)
    private String sequenceNo;

    @Column(columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private String isSubmitted;

    @Column(columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private String isPublished;

    @Column(length = 50)
    private String publishedDate;


//  @OneToMany(mappedBy = "icaComponent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Transient
    private List<IcaCloPloMapping> cloPloMappings = new ArrayList<>();


    @Transient
    private Long icaId; // just for convenience

    @Transient
    private String componentName; // just for view
}
