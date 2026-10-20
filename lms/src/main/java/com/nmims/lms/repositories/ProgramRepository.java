package com.nmims.lms.repositories;

import com.nmims.lms.beans.Program.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProgramRepository  extends JpaRepository<Program , Long> {

    List<Program> findByActive(String active);


    @Query("""
    SELECT DISTINCT p
    FROM Program p
    JOIN p.programCampus pc
    WHERE p.active = 'Y'
      AND p.acadYear = :acadYear
      AND pc.campusId = :campusId
""")
    List<Program> findByAcadYearAndCampusId(
            @Param("acadYear") String acadYear,
            @Param("campusId") String campusId
    );

    @Query("""
    SELECT DISTINCT p
    FROM Program p
    JOIN p.programCampus pc
    WHERE p.active = 'Y'
      AND pc.campusId = :campusId
""")
    List<Program> findByCampusId(

            @Param("campusId") String campusId
    );
}
