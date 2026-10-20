package com.nmims.lms.beans.Ica;

import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Data
public class Ica extends BaseBean {


    @Column(length = 50)
    private String moduleId;

    private Integer acadYear;

    @Column(length = 50)
    private String acadSession;

    @Column(length = 50)
    private String campusId;

    @Column(length = 250)
    private String programId;

    @Column(length = 150)
    private String icaName;

    @Column(name = "icaDesc", length = 500)
    private String icaDesc;

    @Column(length = 500)
    private String assignedFaculty;

    @Column(length = 100)
    private String scaledReq;

    private Double scaledMarks;

    private Integer parentIcaId;

    @Column(length = 150)
    private Long internalMarks;

    @Column(length = 150)
    private Long internalPassMarks;

    @Column(length = 150)
    private Long externalPassMarks;

    @Column(length = 150)
    private Long externalMarks;

    @Column(length = 150)
    private Long totalMarks;

    private String eventId;

    @Column(columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private String isSubmitted;

    @Column(columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private String isPublished;

    @Column(columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private String isIcaDivisionWise;

    @Column(columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private String isNonEventModule;

    @Column(length = 150)
    private String publishedDate;

    private Date startDate;

    private Date endDate;

    @Column(length = 50)
    private String division;

    @Column(columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private String isCourseraIca;

    @Column(columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private String isPublishCompWise;

}
