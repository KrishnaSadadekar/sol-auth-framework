package com.nmims.lms.mapper.ica;

import com.nmims.lms.beans.Ica.IcaCloPloMaster;
import com.nmims.lms.dto.Ica.IcaCloPloMasterDto;
import com.nmims.lms.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IcaCloPloMasterMapper extends BaseMapper<IcaCloPloMaster, IcaCloPloMasterDto> {
//
////    @Mapping(source = "")
//    IcaCloPloMasterDto toDto(IcaCloPloMaster icaCloPloMaster);
//
//    IcaCloPloMaster toEntity(IcaCloPloMasterDto icaCloPloMasterDto);
}
