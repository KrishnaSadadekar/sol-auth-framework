package com.nmims.lms.repositories;

import com.nmims.lms.beans.Ica.IcaQueries;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IcaQueriesRepository extends JpaRepository<IcaQueries,Long> {


    List<IcaQueries> findByIcaIdAndIsReEvaluatedAndIsApproved(
            Long icaId,
            String isReEvaluated,
            String isApproved
    );
}
