package com.nmims.lms.repositories;

import com.nmims.lms.beans.Ica.IcaCloPloMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IcaCloPloRepository extends JpaRepository<IcaCloPloMapping,Long> {
    List<IcaCloPloMapping> findByIcaIdAndComponentId(Long icaId, Long icaComponentId);
}
