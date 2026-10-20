package com.nmims.lms.mapper.ica;

import com.nmims.lms.beans.Ica.IcaCloPloMaster;
import com.nmims.lms.dto.Ica.IcaCloPloMasterDto;
import org.springframework.stereotype.Component;

@Component
public class IcaCloPloMasterMapperImpl implements IcaCloPloMasterMapper
{

    @Override
    public IcaCloPloMasterDto toDto(IcaCloPloMaster entity) {
        if (entity == null) {
            return null;
        }

        IcaCloPloMasterDto dto = new IcaCloPloMasterDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());          // or cloName / ploName
        dto.setType(entity.getType());          // CLO / PLO
        dto.setActive(entity.getActive());


        return dto;
    }

    @Override
    public IcaCloPloMaster toEntity(IcaCloPloMasterDto dto) {
        if (dto == null) {
            return null;
        }

        IcaCloPloMaster entity = new IcaCloPloMaster();

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setActive(dto.getActive());

        return entity;
    }
}

