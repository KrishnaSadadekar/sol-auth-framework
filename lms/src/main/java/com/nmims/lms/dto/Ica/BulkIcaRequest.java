package com.nmims.lms.dto.Ica;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Payload for bulk ICA creation — creates one ICA per moduleId.
 * All fields except moduleIds are shared across all created ICAs.
 */
@Data
public class BulkIcaRequest {

    private List<String> moduleIds;

    private Integer acadYear;
    private String acadSession;
    private String campusId;
    private String programId;
    private String icaName;
    private String description;
    private String assignedFaculty;

    private Long internalMarks;
    private Long externalMarks;
    private Long internalPassMarks;
    private Long externalPassingMarks;
    private Long totalMarks;

    private Date startDate;
    private Date endDate;

    private String division;
    private String isCourseraIca;
    private String isIcaDivisionWise;
    private String isPublishedCompWise;
    private String isNoEventModule;
    private String eventId;
}
