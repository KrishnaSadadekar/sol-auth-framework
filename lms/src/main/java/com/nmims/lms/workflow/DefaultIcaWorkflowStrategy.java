package com.nmims.lms.workflow;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Default workflow for most schools/campuses.
 * - Single ICA creation per page
 * - Component-only mapping (no CLO/PLO)
 * - Integer marks with ROUND rounding
 * - 7-day query window
 * - Total marks submission to SAP
 */
@Component("DEFAULT_WORKFLOW")
public class DefaultIcaWorkflowStrategy implements IcaWorkflowStrategy {

    @Override
    public List<IcaWorkflowStepConfig> getWorkflowSteps() {
        return List.of(
                IcaWorkflowStepConfig.builder()
                        .step(IcaWorkflowStep.CREATE)
                        .enabled(true)
                        .requiresCloPlo(false)
                        .creationMode("SINGLE")
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
                        .queryTimelineDays(7)
                        .build(),
                IcaWorkflowStepConfig.builder()
                        .step(IcaWorkflowStep.CONFIRM_SAP)
                        .enabled(true)
                        .confirmMode("TOTAL_MARKS")
                        .build()
        );
    }
}
