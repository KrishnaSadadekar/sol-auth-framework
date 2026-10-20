package com.nmims.lms.workflow;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcaWorkflowStepConfig {
    private IcaWorkflowStep step;
    private boolean enabled;
    private boolean requiresCloPlo;

    // Step 1 — Create ICA
    private String creationMode;        // "SINGLE", "BULK", "EXCEL_UPLOAD"

    // Step 2 — Add Components
    private boolean componentWithCloPlo; // true = component + CLO-PLO mapping

    // Step 3 — Evaluate ICA
    private String roundingMode;         // "ROUND", "FLOOR", "CEIL", "NONE"
    private String marksInputType;       // "INTEGER", "DOUBLE"

    // Step 4 — Publish ICA
    private boolean publishByAdmin;      // true = admin publishes, students view

    // Step 5 — Resolve Queries
    private Integer queryTimelineDays;

    // Step 6 — Confirm to SAP
    private String confirmMode;          // "TOTAL_MARKS", "COMPONENT_WISE"
}
