package com.nmims.lms.services.Program;

import com.nmims.lms.beans.Program.Program;
import com.nmims.lms.daos.BaseDAO;
import com.nmims.lms.daos.ProgramDAO;
import com.nmims.lms.dto.Program.ProgramDto;
import com.nmims.lms.mapper.program.ProgramMapper;
import com.nmims.lms.repositories.ProgramRepository;
import com.nmims.lms.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("programService")
@RequiredArgsConstructor
public class ProgramService extends BaseService<Program> {

    private final ProgramRepository repository;
    private final ProgramMapper mapper;
    private  final  ProgramDAO programDAO;

    @Override
    protected BaseDAO<Program> getDAO() {
        return programDAO;
    }

    public List<ProgramDto> findByActive(String active)
    {
        return  repository.findByActive(active).stream().map(mapper::toDto).toList();
    }

    public List<ProgramDto> findByAcadYearAndCampusId(String acadYear, String campusId) {
        return repository.findByAcadYearAndCampusId(acadYear,campusId).stream().map(mapper::toDto).toList();
    }

    public List<ProgramDto> findByCampusId(String campusId) {
        return repository.findByCampusId(campusId).stream().map(mapper::toDto).toList();
    }

    public List<Program> findProgramsByAcademicSessionAndAcademicYearAndSubject(String academicSession, Long academicYear, Long moduleId) {
        return  programDAO.findProgramsByAcademicSessionAndAcademicYearAndSubject(academicSession,academicYear,moduleId);
    }



}
