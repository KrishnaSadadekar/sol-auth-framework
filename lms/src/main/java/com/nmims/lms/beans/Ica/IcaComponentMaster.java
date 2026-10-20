package com.nmims.lms.beans.Ica;

import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;



@Data
@Table(name = "pre_def_ica_component")
public class IcaComponentMaster extends BaseBean {

    @Column(nullable = false, unique = true)
    private String componentName;


}
