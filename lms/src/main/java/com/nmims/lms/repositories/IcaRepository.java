package com.nmims.lms.repositories;

import com.nmims.lms.beans.Ica.Ica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IcaRepository extends JpaRepository<Ica,Long> {


    @Modifying
    @Transactional
    @Query("UPDATE Ica i SET i.isSubmitted = :status WHERE i.id = :icaId")
    void updateFinalSubmit(@Param("status") String status, @Param("icaId") Long icaId);
}
