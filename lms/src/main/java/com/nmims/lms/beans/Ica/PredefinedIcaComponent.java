package com.nmims.lms.beans.Ica;

import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pre_def_ica_components")
public class PredefinedIcaComponent extends BaseBean {
    private String componentName;

}
