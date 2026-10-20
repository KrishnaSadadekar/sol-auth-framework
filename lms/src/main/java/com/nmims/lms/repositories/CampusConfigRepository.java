package com.nmims.lms.repositories;

import com.nmims.lms.config.CampusConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
@EnableJpaRepositories
public interface CampusConfigRepository extends JpaRepository<CampusConfig,Long> {
    List<Optional<CampusConfig>> findByCampusCode(String campusCode);


    Optional<CampusConfig> findByCampusCodeAndSchool(String campusCode, String school);

}
