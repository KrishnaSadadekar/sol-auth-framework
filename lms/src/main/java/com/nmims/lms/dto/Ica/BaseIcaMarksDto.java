package com.nmims.lms.dto.Ica;

import lombok.Data;

@Data
public abstract  class BaseIcaMarksDto {

    private String username;

    private Long icaId;

    private Double marks;

    private String saveAsDraft;

    private String finalSubmit;

    private String isQueryRaise;

    private String isQueryApproved;

    private String remarks;

    private String query;

    private String isAbsent;
}
