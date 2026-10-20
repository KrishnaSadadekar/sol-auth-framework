package com.nmims.lms.workflow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRuleRepository extends JpaRepository<WorkflowRule, Long> {

    /** Find campus-specific rules */
    List<WorkflowRule> findBySchoolAndCampusCodeAndActiveOrderByStepAsc(
            String school, String campusCode, String active);

    /** Find school-level rules (campusCode is null) */
    List<WorkflowRule> findBySchoolAndCampusCodeIsNullAndActiveOrderByStepAsc(
            String school, String active);
}
