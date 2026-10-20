package com.nmims.lms.repositories;

import com.nmims.lms.beans.Ica.IcaStudentBatchwise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IcaStudentBatchwiseRepository extends JpaRepository<IcaStudentBatchwise,Long> {


    List<IcaStudentBatchwise> findByIcaId(Long icaId);

    @Query("SELECT COUNT(s) FROM IcaStudentBatchwise s WHERE s.icaId = :icaId")
    int countByIcaId(@Param("icaId") Long icaId);


}
