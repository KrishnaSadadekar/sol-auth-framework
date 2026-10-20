package com.nmims.lms.mapper.ica;

import com.nmims.lms.beans.Ica.IcaComponent;
import com.nmims.lms.dto.Ica.IcaComponentDto;
import org.springframework.stereotype.Component;

@Component
public class IcComponentMapperImpl implements IcaComponentMapper {
    @Override
    public IcaComponentDto toDto(IcaComponent entity) {
        if (entity == null) {
            return null;
        }
        IcaComponentDto dto =new IcaComponentDto();
        dto.setId(entity.getId());
        dto.setMarks(entity.getMarks());
        dto.setComponentName(entity.getComponentName());

        return dto;
    }

    @Override
    public IcaComponent toEntity(IcaComponentDto dto) {
        if(dto==null)
        {
            return null;
        }
        IcaComponent entity =new IcaComponent();
        entity.setComponentId(dto.getId());
        entity.setComponentName(dto.getComponentName());
        entity.setMarks(dto.getMarks());
        return entity;
    }
    @Override
    public void updateEntity(IcaComponentDto dto, IcaComponent entity) {
        entity.setIfNotNull(dto.getComponentName(), entity::setComponentName);
        entity.setIfNotNull(dto.getMarks(), entity::setMarks);
        entity.setIfNotNull(String.valueOf(dto.getSequenceNo()),entity::setSequenceNo);
    }
}
