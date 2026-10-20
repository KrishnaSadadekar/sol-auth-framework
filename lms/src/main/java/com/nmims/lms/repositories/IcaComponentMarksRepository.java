package com.nmims.lms.repositories;

import com.nmims.lms.beans.Ica.IcaComponentMarks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IcaComponentMarksRepository extends JpaRepository<IcaComponentMarks,Long> {
    List<IcaComponentMarks> findByLastModifiedByAndIcaId(String username, Long icaId);
}
