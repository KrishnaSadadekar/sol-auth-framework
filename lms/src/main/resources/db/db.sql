-- =============================================================================
-- LMS Database Schema (db.sql)
-- Target Database: lms_mpstme_nm_m (or local LMS microservice database)
-- Database Engine: MySQL 8.0+ / InnoDB / utf8mb4
-- All JPA Entities in com.nmims.lms are mapped below with complete DDL
-- =============================================================================

SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------------------------------
-- 1. Campus Configuration (CampusConfig.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `campus_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `campus_code` VARCHAR(100) NOT NULL,
  `school` VARCHAR(100) NOT NULL,
  `support_clo_plo` ENUM('Y','N') DEFAULT 'N',
  `special_strategy` VARCHAR(255) DEFAULT NULL,
  `active` ENUM('Y','N') DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_campus_config_campus_school` (`campus_code`, `school`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 2. Workflow Rule Overrides (WorkflowRule.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `workflow_rule` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `school` VARCHAR(100) NOT NULL,
  `campus_code` VARCHAR(100) DEFAULT NULL,
  `step` ENUM('ADD_COMPONENT','CONFIRM_SAP','CREATE','EVALUATE','PUBLISH','RAISE_QUERY') NOT NULL,
  `enabled` ENUM('Y','N') DEFAULT 'Y',
  `creation_mode` VARCHAR(20) DEFAULT NULL,
  `requires_clo_plo` ENUM('Y','N') DEFAULT NULL,
  `component_with_clo_plo` ENUM('Y','N') DEFAULT NULL,
  `rounding_mode` VARCHAR(10) DEFAULT NULL,
  `marks_input_type` VARCHAR(10) DEFAULT NULL,
  `publish_by_admin` ENUM('Y','N') DEFAULT NULL,
  `query_timeline_days` INT DEFAULT NULL,
  `confirm_mode` VARCHAR(20) DEFAULT NULL,
  `active` ENUM('Y','N') DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_workflow_rule` (`school`, `campus_code`, `step`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 3. Predefined ICA Components Catalog (PredefinedIcaComponent.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `pre_def_ica_components` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `componentName` VARCHAR(255) NOT NULL,
  `active` ENUM('Y','N') DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_component_name` (`componentName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 4. CLO / PLO Master Taxonomy (IcaCloPloMaster.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ica_clo_plo_master` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `type` ENUM('CLO','PLO') DEFAULT NULL,
  `parent_id` BIGINT DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `active` ENUM('Y','N') DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_clo_plo_master_name` (`name`),
  KEY `fk_clo_plo_master_parent` (`parent_id`),
  CONSTRAINT `fk_clo_plo_master_parent` FOREIGN KEY (`parent_id`) REFERENCES `ica_clo_plo_master` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 5. Academic Programs (Program.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `program` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `programName` VARCHAR(255) DEFAULT NULL,
  `abbr` VARCHAR(50) DEFAULT NULL,
  `durationInMonths` VARCHAR(50) DEFAULT NULL,
  `maxDurationInMonths` VARCHAR(50) DEFAULT NULL,
  `sessionType` VARCHAR(50) DEFAULT NULL,
  `revisedFromMonth` VARCHAR(50) DEFAULT NULL,
  `revisedFromYear` VARCHAR(50) DEFAULT NULL,
  `acadMonth` VARCHAR(50) DEFAULT NULL,
  `acadYear` VARCHAR(50) DEFAULT NULL,
  `active` ENUM('Y','N') DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_program_abbr` (`abbr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 6. Program to Campus Mapping (ProgramCampus.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `program_campus` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `programId` BIGINT DEFAULT NULL,
  `campusId` VARCHAR(50) DEFAULT NULL,
  `campusAbbr` VARCHAR(50) DEFAULT NULL,
  `campusName` VARCHAR(255) DEFAULT NULL,
  `active` ENUM('Y','N') DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_program_campus_program` (`programId`),
  KEY `idx_program_campus_campusId` (`campusId`),
  CONSTRAINT `fk_program_campus_program` FOREIGN KEY (`programId`) REFERENCES `program` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 7. Courses (Course.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `course` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `abbr` VARCHAR(50) DEFAULT NULL,
  `courseName` VARCHAR(255) DEFAULT NULL,
  `property1` VARCHAR(255) DEFAULT NULL,
  `property2` VARCHAR(255) DEFAULT NULL,
  `property3` VARCHAR(255) DEFAULT NULL,
  `programId` INT DEFAULT NULL,
  `acadYear` SMALLINT DEFAULT NULL,
  `acadMonth` VARCHAR(50) DEFAULT NULL,
  `acadYearCode` VARCHAR(50) DEFAULT NULL,
  `eventName` VARCHAR(255) DEFAULT NULL,
  `acadSession` VARCHAR(50) DEFAULT NULL,
  `moduleId` VARCHAR(50) DEFAULT NULL,
  `moduleName` VARCHAR(255) DEFAULT NULL,
  `campusId` VARCHAR(50) DEFAULT NULL,
  `moduleAbbr` VARCHAR(50) DEFAULT NULL,
  `deptCode` VARCHAR(50) DEFAULT NULL,
  `dept` VARCHAR(100) DEFAULT NULL,
  `moduleCategoryCode` VARCHAR(50) DEFAULT NULL,
  `moduleCategoryName` VARCHAR(255) DEFAULT NULL,
  `active` ENUM('Y','N') DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_course_module_acad` (`moduleId`, `acadYearCode`),
  KEY `idx_course_campus` (`campusId`),
  KEY `idx_course_program` (`programId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 8. Modules (Module.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `module` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `module_id` INT DEFAULT NULL,
  `acadyear` INT DEFAULT NULL,
  `start_date` INT DEFAULT NULL,
  `end_date` INT DEFAULT NULL,
  `sem_total` VARCHAR(50) DEFAULT NULL,
  `oral_total` VARCHAR(50) DEFAULT NULL,
  `prac_total` VARCHAR(50) DEFAULT NULL,
  `internal_total` VARCHAR(50) DEFAULT NULL,
  `oral_prac_total` VARCHAR(50) DEFAULT NULL,
  `term_work_total` VARCHAR(50) DEFAULT NULL,
  `ica_weightage` VARCHAR(50) DEFAULT NULL,
  `term_end_weightage` VARCHAR(50) DEFAULT NULL,
  `module_credit` VARCHAR(50) DEFAULT NULL,
  `module_object_id` VARCHAR(50) DEFAULT NULL,
  `module_abbr` VARCHAR(50) DEFAULT NULL,
  `module_description` VARCHAR(2500) DEFAULT NULL,
  `session_code` VARCHAR(50) DEFAULT NULL,
  `program_id` INT DEFAULT NULL,
  `module_descipline_code` VARCHAR(1500) DEFAULT NULL,
  `module_descipline_name` VARCHAR(1500) DEFAULT NULL,
  `module_category_code` VARCHAR(1500) DEFAULT NULL,
  `module_category_name` VARCHAR(1500) DEFAULT NULL,
  `active` ENUM('Y','N') DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_module_module_id` (`module_id`),
  KEY `idx_module_acadyear` (`acadyear`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 9. User Course Enrollments & Faculty Allocation (UserCourse.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_course` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) NOT NULL,
  `courseId` BIGINT DEFAULT NULL,
  `acadMonth` VARCHAR(50) DEFAULT NULL,
  `acadYear` INT DEFAULT NULL,
  `acadYearCode` INT DEFAULT NULL,
  `acadSession` VARCHAR(50) DEFAULT NULL,
  `role` VARCHAR(50) NOT NULL,
  `active` ENUM('Y','N') DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_course_course` (`courseId`),
  KEY `idx_user_course_user_role` (`username`, `role`),
  CONSTRAINT `fk_user_course_course` FOREIGN KEY (`courseId`) REFERENCES `course` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 10. Local Users Reference Table (for Faculty/Student joins in LMS)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) NOT NULL,
  `firstName` VARCHAR(100) DEFAULT NULL,
  `lastName` VARCHAR(100) DEFAULT NULL,
  `email` VARCHAR(255) DEFAULT NULL,
  `active` ENUM('Y','N') DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_users_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 11. Session Master Reference Table (used by CourseDAO)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `session_master` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `sapSessionCode` VARCHAR(50) NOT NULL,
  `sapSessionText` VARCHAR(100) NOT NULL,
  `active` ENUM('Y','N') DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_sap_session_code` (`sapSessionCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 12. Internal Continuous Assessment (ICA) Master (Ica.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ica` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `moduleId` VARCHAR(50) DEFAULT NULL,
  `acadYear` INT DEFAULT NULL,
  `acadSession` VARCHAR(50) DEFAULT NULL,
  `campusId` VARCHAR(50) DEFAULT NULL,
  `programId` VARCHAR(250) DEFAULT NULL,
  `eventId` VARCHAR(255) DEFAULT NULL,
  `icaName` VARCHAR(150) DEFAULT NULL,
  `icaDesc` VARCHAR(500) DEFAULT NULL,
  `assignedFaculty` VARCHAR(500) DEFAULT NULL,
  `scaledReq` VARCHAR(100) DEFAULT NULL,
  `scaledMarks` DOUBLE DEFAULT NULL,
  `parentIcaId` INT DEFAULT NULL,
  `internalMarks` BIGINT DEFAULT NULL,
  `internalPassMarks` BIGINT DEFAULT NULL,
  `externalPassMarks` BIGINT DEFAULT NULL,
  `externalMarks` BIGINT DEFAULT NULL,
  `totalMarks` BIGINT DEFAULT NULL,
  `active` ENUM('Y','N') DEFAULT 'Y',
  `isSubmitted` ENUM('Y','N') DEFAULT 'N',
  `isPublished` ENUM('Y','N') DEFAULT 'N',
  `isIcaDivisionWise` ENUM('Y','N') DEFAULT 'N',
  `isNonEventModule` ENUM('Y','N') DEFAULT 'N',
  `publishedDate` VARCHAR(150) DEFAULT NULL,
  `startDate` DATETIME(6) DEFAULT NULL,
  `endDate` DATETIME(6) DEFAULT NULL,
  `division` VARCHAR(50) DEFAULT NULL,
  `isCourseraIca` ENUM('Y','N') DEFAULT 'N',
  `isPublishCompWise` ENUM('Y','N') DEFAULT 'N',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_module_acad_campus_session_program` (`moduleId`, `acadYear`, `campusId`, `acadSession`, `programId`, `active`, `eventId`),
  KEY `idx_ica_module_acad` (`moduleId`, `acadYear`, `campusId`),
  KEY `idx_ica_faculty` (`assignedFaculty`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 13. ICA Components (IcaComponent.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ica_components` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `componentId` BIGINT NOT NULL,
  `icaId` BIGINT DEFAULT NULL,
  `marks` DOUBLE DEFAULT NULL,
  `sequenceNo` VARCHAR(50) DEFAULT NULL,
  `isSubmitted` ENUM('Y','N') DEFAULT 'N',
  `isPublished` ENUM('Y','N') DEFAULT 'N',
  `publishedDate` VARCHAR(50) DEFAULT NULL,
  `active` VARCHAR(50) DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `compIdIcaIdUnq` (`componentId`, `icaId`),
  KEY `Ica_Comp_Foreign_Key` (`icaId`),
  KEY `Ica_CompId_Foreign_Key` (`componentId`),
  CONSTRAINT `Ica_Comp_Foreign_Key` FOREIGN KEY (`icaId`) REFERENCES `ica` (`id`) ON DELETE CASCADE,
  CONSTRAINT `Ica_CompId_Foreign_Key` FOREIGN KEY (`componentId`) REFERENCES `pre_def_ica_components` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 14. ICA Component CLO / PLO Mapping (IcaCloPloMapping.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ica_clo_plo_mapping` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `icaId` BIGINT DEFAULT NULL,
  `componentId` BIGINT DEFAULT NULL,
  `cloId` BIGINT DEFAULT NULL,
  `ploId` BIGINT DEFAULT NULL,
  `marks` DOUBLE DEFAULT NULL,
  `active` ENUM('Y','N') DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ica_clo_plo_component` (`componentId`),
  KEY `FK_ica_clo_plo_ica` (`icaId`),
  KEY `FK_ica_clo_plo_clo` (`cloId`),
  KEY `FK_ica_clo_plo_plo` (`ploId`),
  CONSTRAINT `FK_ica_clo_plo_component` FOREIGN KEY (`componentId`) REFERENCES `ica_components` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_ica_clo_plo_ica` FOREIGN KEY (`icaId`) REFERENCES `ica` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_ica_clo_plo_clo` FOREIGN KEY (`cloId`) REFERENCES `ica_clo_plo_master` (`id`) ON DELETE SET NULL,
  CONSTRAINT `FK_ica_clo_plo_plo` FOREIGN KEY (`ploId`) REFERENCES `ica_clo_plo_master` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 15. ICA Component Student Marks (IcaComponentMarks.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ica_component_marks` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) DEFAULT NULL,
  `icaId` BIGINT DEFAULT NULL,
  `componentId` BIGINT DEFAULT NULL,
  `marks` DOUBLE DEFAULT NULL,
  `saveAsDraft` ENUM('Y','N') DEFAULT 'N',
  `finalSubmit` ENUM('Y','N') DEFAULT 'N',
  `isQueryRaise` ENUM('Y','N') DEFAULT 'N',
  `isQueryApproved` ENUM('Y','N') DEFAULT 'N',
  `remarks` VARCHAR(500) DEFAULT NULL,
  `query` VARCHAR(5000) DEFAULT NULL,
  `isAbsent` ENUM('Y','N') DEFAULT 'N',
  `active` VARCHAR(50) DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_icaId_componentId` (`username`, `icaId`, `componentId`),
  KEY `icaIdForeignKey` (`icaId`),
  KEY `compIdFK` (`componentId`),
  CONSTRAINT `icaIdForeignKey` FOREIGN KEY (`icaId`) REFERENCES `ica` (`id`) ON DELETE CASCADE,
  CONSTRAINT `compIdFK` FOREIGN KEY (`componentId`) REFERENCES `pre_def_ica_components` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 16. ICA CLO / PLO Student Marks (IcaCloPloMarks.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ica_clo_plo_marks` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) DEFAULT NULL,
  `icaId` BIGINT DEFAULT NULL,
  `componentId` BIGINT DEFAULT NULL,
  `cloId` BIGINT DEFAULT NULL,
  `ploId` BIGINT DEFAULT NULL,
  `marks` DOUBLE DEFAULT NULL,
  `saveAsDraft` ENUM('Y','N') DEFAULT 'N',
  `finalSubmit` ENUM('Y','N') DEFAULT 'N',
  `isQueryRaise` ENUM('Y','N') DEFAULT 'N',
  `isQueryApproved` ENUM('Y','N') DEFAULT 'N',
  `remarks` VARCHAR(500) DEFAULT NULL,
  `query` VARCHAR(5000) DEFAULT NULL,
  `isAbsent` ENUM('Y','N') DEFAULT 'N',
  `active` VARCHAR(50) DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_clo_plo_marks_ica` (`icaId`),
  KEY `fk_clo_plo_marks_comp` (`componentId`),
  KEY `fk_clo_plo_marks_clo` (`cloId`),
  KEY `fk_clo_plo_marks_plo` (`ploId`),
  CONSTRAINT `fk_clo_plo_marks_ica` FOREIGN KEY (`icaId`) REFERENCES `ica` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_clo_plo_marks_comp` FOREIGN KEY (`componentId`) REFERENCES `ica_components` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_clo_plo_marks_clo` FOREIGN KEY (`cloId`) REFERENCES `ica_clo_plo_master` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_clo_plo_marks_plo` FOREIGN KEY (`ploId`) REFERENCES `ica_clo_plo_master` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 17. ICA Total Scaled Student Marks (IcaTotalMarks.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ica_total_marks` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) DEFAULT NULL,
  `icaId` BIGINT DEFAULT NULL,
  `marks` DOUBLE DEFAULT NULL,
  `icaScaledMarks` DOUBLE DEFAULT NULL,
  `saveAsDraft` ENUM('Y','N') DEFAULT 'N',
  `finalSubmit` ENUM('Y','N') DEFAULT 'N',
  `isQueryRaise` ENUM('Y','N') DEFAULT 'N',
  `isQueryApproved` ENUM('Y','N') DEFAULT 'N',
  `remarks` VARCHAR(500) DEFAULT NULL,
  `query` VARCHAR(5000) DEFAULT NULL,
  `isAbsent` ENUM('Y','N') DEFAULT 'N',
  `active` VARCHAR(50) DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ica_total_marks_user_ica` (`username`, `icaId`),
  KEY `fk_ica_total_marks_ica` (`icaId`),
  CONSTRAINT `fk_ica_total_marks_ica` FOREIGN KEY (`icaId`) REFERENCES `ica` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 18. ICA Student Queries & Grievances (IcaQueries.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ica_queries` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `icaId` BIGINT DEFAULT NULL,
  `componentId` BIGINT DEFAULT NULL,
  `filePath` VARCHAR(255) DEFAULT NULL,
  `isApproved` VARCHAR(50) DEFAULT 'N',
  `isReEvaluated` VARCHAR(50) DEFAULT 'N',
  `active` ENUM('Y','N') DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ica_queries_ica` (`icaId`),
  KEY `fk_ica_queries_comp` (`componentId`),
  CONSTRAINT `fk_ica_queries_ica` FOREIGN KEY (`icaId`) REFERENCES `ica` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ica_queries_comp` FOREIGN KEY (`componentId`) REFERENCES `pre_def_ica_components` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 19. ICA Student Batch Allocation (IcaStudentBatchwise.java)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `ica_student_batchwise` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `icaId` BIGINT DEFAULT NULL,
  `facultyId` VARCHAR(100) DEFAULT NULL,
  `username` VARCHAR(100) DEFAULT NULL,
  `active` ENUM('Y','N') DEFAULT 'Y',
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_student_batchwise_ica` (`icaId`),
  KEY `idx_student_batchwise_username` (`username`),
  CONSTRAINT `fk_student_batchwise_ica` FOREIGN KEY (`icaId`) REFERENCES `ica` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- 20. Compatibility Views for Legacy DAO Query Names
-- -----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `usercourse` AS SELECT * FROM `user_course`;
CREATE OR REPLACE VIEW `clo_plo_master` AS SELECT * FROM `ica_clo_plo_master`;

SET FOREIGN_KEY_CHECKS = 1;

-- =============================================================================
-- Seed & Initial Reference Data
-- =============================================================================

-- Campus Configuration & School Workflow Strategies
INSERT INTO `campus_config` (`campus_code`, `school`, `special_strategy`, `support_clo_plo`, `active`)
VALUES
  ('DEFAULT',  'DEFAULT',        NULL,       'N', 'Y'),
  ('MUM01',    'SBM-NM-M',      'SBM',      'Y', 'Y'),
  ('MUM01',    'MPSTME-NM-M',   NULL,       'N', 'Y'),
  ('MUM01',    'SOE-NM-M',      'SOE',      'N', 'Y'),
  ('MUM01',    'PHARMACY-NM-M', 'PHARMACY', 'Y', 'Y')
ON DUPLICATE KEY UPDATE `special_strategy` = VALUES(`special_strategy`);

-- Standard Predefined ICA Components
INSERT INTO `pre_def_ica_components` (`componentName`, `active`)
VALUES
  ('Assignment', 'Y'),
  ('Class Participation', 'Y'),
  ('Mid Term Test', 'Y'),
  ('Project', 'Y'),
  ('Quiz', 'Y'),
  ('Presentation', 'Y'),
  ('Lab Work / Practical', 'Y'),
  ('Case Study', 'Y')
ON DUPLICATE KEY UPDATE `active` = VALUES(`active`);