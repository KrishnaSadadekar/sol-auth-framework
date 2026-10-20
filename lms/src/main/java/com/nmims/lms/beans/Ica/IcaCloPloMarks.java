package com.nmims.lms.beans.Ica;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="ica_clo_plo_marks")
public class IcaCloPloMarks  extends BaseIcaMarks {
    private Long componentId;

    private Long cloId;
    private Long ploId;



}
