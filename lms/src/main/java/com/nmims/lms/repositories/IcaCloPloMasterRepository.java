package com.nmims.lms.repositories;

import com.nmims.lms.beans.Ica.IcaCloPloMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IcaCloPloMasterRepository  extends JpaRepository<IcaCloPloMaster,Long> {


    @Query("SELECT c FROM IcaCloPloMaster c WHERE c.type = :type AND c.parentId IS NULL")
    List<IcaCloPloMaster> findByTypeAndParentIsNull(@Param("type") String type);

    @Query("SELECT c FROM IcaCloPloMaster c WHERE  c.parentId IS NULL")
    List<IcaCloPloMaster> findByParentIsNull();


}
