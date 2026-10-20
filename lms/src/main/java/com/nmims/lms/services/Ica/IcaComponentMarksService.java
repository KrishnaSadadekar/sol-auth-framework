package com.nmims.lms.services.Ica;

import com.nmims.lms.beans.Ica.IcaComponentMarks;
import com.nmims.lms.daos.BaseDAO;
import com.nmims.lms.repositories.IcaComponentMarksRepository;
import com.nmims.lms.services.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("icaComponentMarksService")
@Slf4j
@RequiredArgsConstructor
public class IcaComponentMarksService extends BaseService<IcaComponentMarks> {


    @Override
    protected BaseDAO<IcaComponentMarks> getDAO() {
        return null;
    }

    @Autowired
    IcaComponentMarksRepository icaComponentMarksRepository;


    public IcaComponentMarks save(IcaComponentMarks comp) {
        return icaComponentMarksRepository.save(comp);
    }

    public List<IcaComponentMarks> findByLastModifiedByAndIcaId(String username, Long icaId) {
        return icaComponentMarksRepository.findByLastModifiedByAndIcaId(username,icaId);
    }
}
