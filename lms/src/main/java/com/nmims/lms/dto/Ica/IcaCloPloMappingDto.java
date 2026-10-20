package com.nmims.lms.dto.Ica;

import com.nmims.lms.beans.BaseBean;
import com.nmims.lms.beans.Ica.IcaComponent;
import jakarta.persistence.Entity;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class IcaCloPloMappingDto  {


    private Long cloId;
    private Long ploId;
    private IcaComponent icaComponent;
    private Long componentId;
    private Double marks;
    private String cloName;
    private  String ploName ;


    public IcaCloPloMappingDto(Long cloId, Long ploId, Long componentId, Double marks) {
        this.cloId = cloId;
        this.ploId = ploId;
        this.componentId = componentId;
        this.marks = marks;
    }
}
