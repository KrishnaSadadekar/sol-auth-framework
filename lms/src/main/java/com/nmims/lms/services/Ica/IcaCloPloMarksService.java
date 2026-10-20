package com.nmims.lms.services.Ica;

import com.nmims.lms.beans.Ica.IcaCloPloMarks;
import com.nmims.lms.daos.BaseDAO;
import com.nmims.lms.repositories.IcaCloPloMarksRepository;
import com.nmims.lms.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("icaCloPloMarksService")
public class IcaCloPloMarksService  extends BaseService<IcaCloPloMarks> {
    @Override
    protected BaseDAO<IcaCloPloMarks> getDAO() {
        return null;
    }
    @Autowired
    IcaCloPloMarksRepository icaCloPloMarksRepository;


    public IcaCloPloMarks save(IcaCloPloMarks icaCloPloMarks)
    {
        return  icaCloPloMarksRepository.save(icaCloPloMarks);
    }

    public List<IcaCloPloMarks> saveAll(List<IcaCloPloMarks> icaCloPloMarks)
    {
        return  icaCloPloMarksRepository.saveAll(icaCloPloMarks);
    }

}
