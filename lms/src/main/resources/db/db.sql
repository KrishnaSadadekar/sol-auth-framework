CREATE TABLE `ica` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `moduleId` varchar(50) DEFAULT NULL,
  `acadYear` int DEFAULT NULL,
  `campusId` varchar(50) DEFAULT NULL,
  `programId` varchar(250) DEFAULT NULL,
  `acadSession` varchar(50) DEFAULT NULL,
  `eventId` varchar(255) DEFAULT NULL,
  `icaName` varchar(150) DEFAULT NULL,
  `icaDesc` varchar(500) DEFAULT NULL,
  `assignedFaculty` varchar(500) DEFAULT NULL,
  `scaledReq` varchar(100) DEFAULT NULL,
  `scaledMarks` int DEFAULT NULL,
  `parentIcaId` int DEFAULT NULL,
  `internalMarks` bigint DEFAULT NULL,
  `internalPassMarks` bigint DEFAULT NULL,
  `externalPassMarks` bigint DEFAULT NULL,
  `externalMarks` bigint DEFAULT NULL,
  `totalMarks` bigint DEFAULT NULL,
  `active` enum('Y','N') DEFAULT 'Y',
  `isSubmitted` enum('Y','N') DEFAULT 'N',
  `isPublished` enum('Y','N') DEFAULT 'N',
  `isIcaDivisionWise` enum('Y','N') DEFAULT 'N',
  `isNonEventModule` enum('Y','N') DEFAULT 'N',
  `publishedDate` varchar(150) DEFAULT NULL,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `lastModifiedBy` varchar(255) DEFAULT NULL,
  `lastModifiedDate` datetime DEFAULT NULL,
  `isCourseraIca` enum('Y','N') DEFAULT 'N',
  `isPublishCompWise` enum('Y','N') DEFAULT 'N',
  `division` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci




CREATE TABLE `ica_components` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `componentId` bigint NOT NULL,
  `icaId` bigint DEFAULT NULL,
  `marks` double DEFAULT NULL,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `lastModifiedBy` varchar(255) DEFAULT NULL,
  `lastModifiedDate` datetime DEFAULT NULL,
  `active` enum('Y','N') DEFAULT 'Y',
  `sequenceNo` varchar(50) DEFAULT NULL,
  `isSubmitted` enum('Y','N') DEFAULT 'N',
  `isPublished` enum('Y','N') DEFAULT 'N',
  `publishedDate` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `compIdIcaIdUnq` (`componentId`,`icaId`),
  KEY `Ica_Comp_Foreign_Key` (`icaId`),
  CONSTRAINT `Ica_Comp_Foreign_Key` FOREIGN KEY (`icaId`) REFERENCES `ica` (`id`),
  CONSTRAINT `Ica_CompId_Foreign_Key` FOREIGN KEY (`componentId`) REFERENCES `pre_def_ica_components` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci



CREATE TABLE `ica_clo_plo_mapping` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `icaId` bigint DEFAULT NULL,
  `componentId` bigint DEFAULT NULL,
  `cloId` bigint DEFAULT NULL,
  `ploId` bigint DEFAULT NULL,
  `marks` double DEFAULT NULL,
  `active` enum('Y','N') DEFAULT 'Y',
  `createdBy` varchar(255) DEFAULT NULL,
  `createdDate` datetime(6) DEFAULT NULL,
  `lastModifiedBy` varchar(255) DEFAULT NULL,
  `lastModifiedDate` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ica_clo_plo_component` (`componentId`),
  KEY `FK_ica_clo_plo_ica` (`icaId`),
  CONSTRAINT `FK_ica_clo_plo_component` FOREIGN KEY (`componentId`) REFERENCES `ica_components` (`id`),
  CONSTRAINT `FK_ica_clo_plo_ica` FOREIGN KEY (`icaId`) REFERENCES `ica` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


ALTER TABLE ica
ADD CONSTRAINT unique_module_acad_campus_session_program
UNIQUE (module_id, acad_year, campus_id, acad_session, program_id);

CREATE TABLE `campus_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` enum('Y','N') DEFAULT 'Y',
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `campus_code` varchar(100) NOT NULL,
  `school` varchar(100) NOT NULL,
  `special_strategy` varchar(255) DEFAULT NULL,
  `support_clo_plo` enum('Y','N') DEFAULT 'N',
  `createdBy` varchar(255) DEFAULT NULL,
  `createdDate` datetime(6) DEFAULT NULL,
  `lastModifiedBy` varchar(255) DEFAULT NULL,
  `lastModifiedDate` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `ica_clo_plo_master` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `active` enum('Y','N') DEFAULT 'Y',
  `createdBy` varchar(255) DEFAULT NULL,
  `createdDate` datetime(6) DEFAULT NULL,
  `lastModifiedBy` varchar(255) DEFAULT NULL,
  `lastModifiedDate` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `pre_def_ica_components` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `component_name` varchar(255) NOT NULL,
  `active` enum('Y','N') DEFAULT 'Y',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_component_name` (`component_name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

ALTER TABLE  ica_clo_plo_master ADD CONSTRAINT qunique_name UNIQUE(name)
ALTER TABLE ica_clo_plo_master MODIFY COLUMN type ENUM('CLO', 'PLO') DEFAULT NULL;





--Krisha 1-11-2025
UNIQUE KEY `moduleId_acadYear_campusId_programId` (`moduleId`,`acadYear`,`campusId`,`acadSession`,`programId`,`active`,`eventId`)

CREATE TABLE `ica_clo_plo_mapping` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  -- Core business relationships
  `icaId` BIGINT DEFAULT NULL,
  `componentId` BIGINT DEFAULT NULL,

  -- CLO/PLO mapping details
  `cloId` BIGINT DEFAULT NULL,
  `ploId` BIGINT DEFAULT NULL,
  `marks` DOUBLE DEFAULT NULL,

  -- Status flag
  `active` ENUM('Y','N') DEFAULT 'Y',

  -- Audit fields
  `createdBy` VARCHAR(255) DEFAULT NULL,
  `createdDate` DATETIME(6) DEFAULT NULL,
  `lastModifiedBy` VARCHAR(255) DEFAULT NULL,
  `lastModifiedDate` DATETIME(6) DEFAULT NULL,

  PRIMARY KEY (`id`),

  -- Foreign key constraints
  KEY `FK_ica_clo_plo_component` (`componentId`),
  KEY `FK_ica_clo_plo_ica` (`icaId`),

  CONSTRAINT `FK_ica_clo_plo_component`
    FOREIGN KEY (`componentId`) REFERENCES `ica_components` (`id`),

  CONSTRAINT `FK_ica_clo_plo_ica`
    FOREIGN KEY (`icaId`) REFERENCES `ica` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
-- Marks table


CREATE TABLE `ica_component_marks` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `icaId` bigint DEFAULT NULL,
  `componentId` bigint DEFAULT NULL,
  `marks` double DEFAULT NULL,
  `saveAsDraft` enum('Y','N') DEFAULT NULL,
  `finalSubmit` enum('Y','N') DEFAULT NULL,
  `isQueryRaise` enum('Y','N') DEFAULT 'N',
  `isQueryApproved` enum('Y','N') DEFAULT 'N',
  `remarks` varchar(500) DEFAULT NULL,
  `query` varchar(5000) DEFAULT NULL,
  `active` varchar(50) DEFAULT 'Y',
  `createdBy` varchar(100) DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `lastModifiedBy` varchar(100) DEFAULT NULL,
  `lastModifiedDate` datetime DEFAULT NULL,
  `isAbsent` enum('Y','N') DEFAULT 'N',
  PRIMARY KEY (`id`),

  KEY `icaIdForeignKey` (`icaId`),
  KEY `compIdFK` (`componentId`),
  CONSTRAINT `compIdFK` FOREIGN KEY (`componentId`) REFERENCES `pre_def_ica_components` (`id`),
  CONSTRAINT `icaIdForeignKey` FOREIGN KEY (`icaId`) REFERENCES `ica` (`id`)
)


UNIQUE KEY `username_icaId_componentId` (`username`,`icaId`,`componentId`),


-- ─────────────────────────────────────────────────────
-- Campus Config: Sample school strategies
-- special_strategy maps to @Component bean names in workflow package
-- ─────────────────────────────────────────────────────

INSERT INTO campus_config (campus_code, school, special_strategy, support_clo_plo, active)
VALUES
  ('DEFAULT',  'DEFAULT',        NULL,       'N', 'Y'),
  ('MUM01',    'SBM-NM-M',      'SBM',      'Y', 'Y'),
  ('MUM01',    'MPSTME-NM-M',   NULL,       'N', 'Y'),
  ('MUM01',    'SOE-NM-M',      'SOE',      'N', 'Y'),
  ('MUM01',    'PHARMACY-NM-M', 'PHARMACY', 'Y', 'Y')
ON DUPLICATE KEY UPDATE special_strategy = VALUES(special_strategy);