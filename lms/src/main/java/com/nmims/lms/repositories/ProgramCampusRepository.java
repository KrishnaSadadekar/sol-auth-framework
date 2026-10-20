package com.nmims.lms.repositories;

import com.nmims.lms.beans.Program.ProgramCampus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramCampusRepository extends JpaRepository<ProgramCampus ,Long> {


}
