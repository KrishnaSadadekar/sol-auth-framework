package com.nmims.lms.mapper.program;

import com.nmims.lms.beans.Program.Program;
import com.nmims.lms.dto.Program.ProgramDto;
import org.springframework.stereotype.Component;

@Component
public class ProgramMapperImpl implements ProgramMapper {
    @Override
    public ProgramDto toDto(Program entity) {
        if(entity==null)return  null;
        ProgramDto dto =new ProgramDto();
        dto.setProgramName(entity.getProgramName());
        dto.setAbbr(entity.getAbbr());
        dto.setId(entity.getId());
        dto.setAcadYear(entity.getAcadYear());
        dto.setActive(entity.getActive());

        return dto;
    }

    @Override
    public Program toEntity(ProgramDto dto) {
        if(dto==null)return  null;
        Program entity =new Program();
        entity.setAbbr(dto.getAbbr());
        entity.setProgramName(dto.getProgramName());
        entity.setActive(dto.getActive());
        return entity;
    }
}
