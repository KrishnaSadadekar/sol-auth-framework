package com.nmims.lms.dto.Ica;

import lombok.Data;

import java.util.List;

@Data
public class IcaTotalMarksDto extends  BaseIcaMarksDto{

    List<IcaComponentMarksDto> icaComponentMarksDtos;

}
