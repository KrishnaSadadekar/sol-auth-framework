package com.nmims.lms.services.Ica;

import com.nmims.lms.beans.Ica.IcaQueries;
import com.nmims.lms.daos.BaseDAO;
import com.nmims.lms.repositories.IcaQueriesRepository;
import com.nmims.lms.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("icaQueriesService")
public class IcaQueriesService extends BaseService<IcaQueries> {


    @Override
    protected BaseDAO<IcaQueries> getDAO() {
        return null;
    }

    @Autowired
    IcaQueriesRepository icaQueriesRepository;

    public List<IcaQueries> findByIcaIdAndIsReEvaluatedAndIsApproved(Long icaId, String isReEvaluated, String isApproved) {
        return  icaQueriesRepository.findByIcaIdAndIsReEvaluatedAndIsApproved(icaId,isReEvaluated,isApproved);
    }

    public void saveAll(List<IcaQueries> queryRaisedStudents) {
        icaQueriesRepository.saveAll(queryRaisedStudents);
    }
}
