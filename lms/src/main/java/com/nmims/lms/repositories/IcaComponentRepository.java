package com.nmims.lms.repositories;

import com.nmims.lms.beans.Ica.Ica;
import com.nmims.lms.beans.Ica.IcaComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IcaComponentRepository extends  JpaRepository<IcaComponent,Long> {

    public List<IcaComponent> findByIca(Ica ica);

    @Query("SELECT ic FROM IcaComponent ic WHERE ic.ica.id = :icaId")
    List<IcaComponent> findByIcaId(@Param("icaId") Long icaId);
}
