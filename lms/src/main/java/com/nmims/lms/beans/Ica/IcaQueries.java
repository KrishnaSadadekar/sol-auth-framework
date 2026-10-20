package com.nmims.lms.beans.Ica;

import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "ica_queries")
public class IcaQueries extends BaseBean {


    private Long icaId;
    private String filePath;
    private String isApproved;
    private String isReEvaluated;
    private Long componentId;
}
