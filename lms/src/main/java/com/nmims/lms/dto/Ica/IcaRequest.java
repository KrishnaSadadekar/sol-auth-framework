package com.nmims.lms.dto.Ica;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class IcaRequest {

    private String moduleId;             // Module identifier
    private Integer acadYear;             // Academic year
    private String acadSession;          // Academic session
    private String campusId;             // Campus identifier
    private String programId;            // Program identifier

    private String icaName;              // ICA title or name
    private String description;          // ICA description
    private String assignedFaculty;      // Faculty assigned for ICA

    private String isSubmitted;          // 'Y' or 'N' if ICA is submitted

    private Date publishedDate;          // Date when ICA is published
    private String eventId;              // Event identifier
    private String parentIcaId;          // Parent ICA ID (if any)

    private Long internalMarks;          // Internal marks
    private Long externalMarks;          // External marks
    private Long internalPassMarks;   // Internal passing marks
    private Long externalPassingMarks;   // External passing marks
    private Long totalMarks;             // Total marks

    private Date startDate;              // ICA start date
    private Date endDate;                // ICA end date

    private String division;             // Division info

    private String isCourseraIca;        // 'Y' or 'N' if Coursera ICA
    private String isIcaDivisionWise;    // 'Y' or 'N' if division-wise
    private String isPublishedCompWise;  // 'Y' or 'N' if published component-wise
    private String isNoEventModule;
    private Long icaId;

    private List<IcaComponentDto> icaComponentList;

}
