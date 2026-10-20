package com.nmims.lms.dto.Ica;

import lombok.Data;

import java.util.List;

@Data
public class IcaComponentMarksDto extends  BaseIcaMarksDto {

    private Long componentId;
    private  List<IcaCloPloMarksDto> icaCloPloMarksDtos;

}
