package com.nmims.lms.beans.Ica;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "ica_clo_plo_mapping")
public class IcaCloPloMapping extends BaseBean {

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "componentId")
//    private IcaComponent icaComponent;

    private Long componentId;
    private Long cloId;
    private Long ploId;
    private Double marks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icaId")
    @JsonIgnore
    private Ica ica;


    //    @OneToMany(mappedBy = "icaCloPloMapping", cascade = CascadeType.ALL, orphanRemoval = true)
    //    private List<IcaCloPloMaster> icaCloPloMaster;


}
