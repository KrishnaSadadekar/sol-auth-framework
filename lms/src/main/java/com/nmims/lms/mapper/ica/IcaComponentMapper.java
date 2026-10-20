package com.nmims.lms.mapper.ica;

import com.nmims.lms.beans.Ica.IcaComponent;
import com.nmims.lms.dto.Ica.IcaComponentDto;
import com.nmims.lms.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcaComponentMapper extends BaseMapper<IcaComponent, IcaComponentDto> {

   public void updateEntity(IcaComponentDto dto, IcaComponent entity);
}
