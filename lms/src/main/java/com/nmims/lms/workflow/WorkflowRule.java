package com.nmims.lms.workflow;

import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Database-backed workflow rule that can override strategy defaults.
 * Resolution priority: campus-specific rule > school-level rule > strategy fallback.
 */
@Entity
@Data
@Table(name = "workflow_rule",
       uniqueConstraints = @UniqueConstraint(
               columnNames = {"school", "campus_code", "step"}))
public class WorkflowRule extends BaseBean {

    @Column(nullable = false, length = 100)
    private String school;

    /** Null means "applies to all campuses for this school" */
    @Column(name = "campus_code", length = 100)
    private String campusCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private IcaWorkflowStep step;

    @Column(columnDefinition = "ENUM('Y','N') DEFAULT 'Y'")
    private String enabled;

    /** "SINGLE", "BULK", "EXCEL_UPLOAD" */
    @Column(name = "creation_mode", length = 20)
    private String creationMode;

    @Column(name = "requires_clo_plo", columnDefinition = "ENUM('Y','N')")
    private String requiresCloPlo;

    @Column(name = "component_with_clo_plo", columnDefinition = "ENUM('Y','N')")
    private String componentWithCloPlo;

    /** "ROUND", "FLOOR", "CEIL", "NONE" */
    @Column(name = "rounding_mode", length = 10)
    private String roundingMode;

    /** "INTEGER", "DOUBLE" */
    @Column(name = "marks_input_type", length = 10)
    private String marksInputType;

    @Column(name = "publish_by_admin", columnDefinition = "ENUM('Y','N')")
    private String publishByAdmin;

    @Column(name = "query_timeline_days")
    private Integer queryTimelineDays;

    /** "TOTAL_MARKS", "COMPONENT_WISE" */
    @Column(name = "confirm_mode", length = 20)
    private String confirmMode;
}
