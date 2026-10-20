package com.nmims.lms.mapper.program;

import com.nmims.lms.beans.Program.Program;
import com.nmims.lms.dto.Program.ProgramDto;
import com.nmims.lms.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProgramMapper  extends BaseMapper<Program , ProgramDto> {

}
