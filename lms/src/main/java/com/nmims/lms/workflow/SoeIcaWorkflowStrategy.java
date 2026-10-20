package com.nmims.lms.workflow;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * SOE (School of Engineering) workflow strategy.
 * - Bulk ICA creation (multiple ICAs on single page)
 * - Component-only mapping (no CLO/PLO)
 * - Integer marks with ROUND rounding
 * - 10-day query timeline
 * - Total marks submission to SAP
 */
@Component("SOE_WORKFLOW")
public class SoeIcaWorkflowStrategy implements IcaWorkflowStrategy {

    @Override
    public List<IcaWorkflowStepConfig> getWorkflowSteps() {
        return List.of(
                IcaWorkflowStepConfig.builder()
                        .step(IcaWorkflowStep.CREATE)
                        .enabled(true)
                        .requiresCloPlo(false)
                        .creationMode("BULK")
                        .build(),
                IcaWorkflowStepConfig.builder()
                        .step(IcaWorkflowStep.ADD_COMPONENT)
                        .enabled(true)
                        .requiresCloPlo(false)
                        .componentWithCloPlo(false)
                        .build(),
                IcaWorkflowStepConfig.builder()
                        .step(IcaWorkflowStep.EVALUATE)
                        .enabled(true)
                        .requiresCloPlo(false)
                        .roundingMode("ROUND")
                        .marksInputType("INTEGER")
                        .build(),
                IcaWorkflowStepConfig.builder()
                        .step(IcaWorkflowStep.PUBLISH)
                        .enabled(true)
                        .publishByAdmin(true)
                        .build(),
                IcaWorkflowStepConfig.builder()
                        .step(IcaWorkflowStep.RAISE_QUERY)
                        .enabled(true)
                        .queryTimelineDays(10)
                        .build(),
                IcaWorkflowStepConfig.builder()
                        .step(IcaWorkflowStep.CONFIRM_SAP)
                        .enabled(true)
                        .confirmMode("TOTAL_MARKS")
                        .build()
        );
    }
}
