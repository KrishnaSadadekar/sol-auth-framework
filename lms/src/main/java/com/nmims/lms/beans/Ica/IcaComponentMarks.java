package com.nmims.lms.beans.Ica;

import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "ica_component_marks")
public class IcaComponentMarks extends BaseIcaMarks {
    @Transient
    private List<IcaCloPloMarks> icaCloPloMarks;
    private Long componentId;


}

