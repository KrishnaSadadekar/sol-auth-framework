package com.nmims.lms.services.Program;

import com.nmims.lms.beans.Program.ProgramCampus;
import com.nmims.lms.daos.BaseDAO;
import com.nmims.lms.daos.ProgramCampusDAO;
import com.nmims.lms.repositories.ProgramCampusRepository;
import com.nmims.lms.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramCampusService extends BaseService<ProgramCampus> {

    private final ProgramCampusRepository repository;
    private  final ProgramCampusDAO programCampusDAO;
    @Override
    protected BaseDAO<ProgramCampus> getDAO() {
        return programCampusDAO;
    }


    public List<ProgramCampus> findAll() {
        return  repository.findAll();
    }

    public List<ProgramCampus> findAllCampuses() {
       return  programCampusDAO.findAllCampuses();
    }


}
