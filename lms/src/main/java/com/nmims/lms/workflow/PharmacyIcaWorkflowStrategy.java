package com.nmims.lms.workflow;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Pharmacy school workflow strategy.
 * - Excel upload for ICA creation
 * - Component mapping with CLO/PLO
 * - Double marks with FLOOR rounding
 * - 21-day query timeline
 * - Component-wise marks submission to SAP
 */
@Component("PHARMACY_WORKFLOW")
public class PharmacyIcaWorkflowStrategy implements IcaWorkflowStrategy {

    @Override
    public List<IcaWorkflowStepConfig> getWorkflowSteps() {
        return List.of(
                IcaWorkflowStepConfig.builder()
                        .step(IcaWorkflowStep.CREATE)
                        .enabled(true)
                        .requiresCloPlo(true)
                        .creationMode("EXCEL_UPLOAD")
                        .build(),
                IcaWorkflowStepConfig.builder()
                        .step(IcaWorkflowStep.ADD_COMPONENT)
                        .enabled(true)
                        .requiresCloPlo(true)
                        .componentWithCloPlo(true)
                        .build(),
                IcaWorkflowStepConfig.builder()
                        .step(IcaWorkflowStep.EVALUATE)
                        .enabled(true)
                        .requiresCloPlo(true)
                        .roundingMode("FLOOR")
                        .marksInputType("DOUBLE")
                        .build(),
                IcaWorkflowStepConfig.builder()
                        .step(IcaWorkflowStep.PUBLISH)
                        .enabled(true)
                        .publishByAdmin(true)
                        .build(),
                IcaWorkflowStepConfig.builder()
                        .step(IcaWorkflowStep.RAISE_QUERY)
                        .enabled(true)
                        .queryTimelineDays(21)
                        .build(),
                IcaWorkflowStepConfig.builder()
                        .step(IcaWorkflowStep.CONFIRM_SAP)
                        .enabled(true)
                        .confirmMode("COMPONENT_WISE")
                        .build()
        );
    }
}
