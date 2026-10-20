package com.nmims.lms.services.Ica;

import com.nmims.lms.beans.Ica.IcaStudentBatchwise;
import com.nmims.lms.daos.BaseDAO;
import com.nmims.lms.repositories.IcaStudentBatchwiseRepository;
import com.nmims.lms.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("icaStudentBatchwiseService")
public class IcaStudentBatchwiseService extends BaseService<IcaStudentBatchwise> {
    @Override
    protected BaseDAO<IcaStudentBatchwise> getDAO() {
        return null;
    }

    @Autowired
    IcaStudentBatchwiseRepository icaStudentBatchwiseRepository;

    public int countByIcaId(Long icaId) {
        return  icaStudentBatchwiseRepository.countByIcaId(icaId);
    }
}
