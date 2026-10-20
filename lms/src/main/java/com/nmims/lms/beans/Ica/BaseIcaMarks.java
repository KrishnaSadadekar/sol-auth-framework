package com.nmims.lms.beans.Ica;

import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class BaseIcaMarks  extends BaseBean {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icaId", referencedColumnName = "id")
    private Ica ica;

    private String username;

    private Double marks;

    private String saveAsDraft;

    private String finalSubmit;

    private String isQueryRaise;

    private String isQueryApproved;

    private String remarks;

    private String query;

    private String isAbsent;

}
