package com.nmims.lms.workflow;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * SBM school workflow strategy.
 * - Single ICA creation
 * - CLO/PLO mapping required with components
 * - Double (floating-point) marks, no rounding
 * - 14-day query timeline
 * - Component-wise marks submission to SAP
 */
@Component("SBM_WORKFLOW")
public class SbmIcaWorkflowStrategy implements IcaWorkflowStrategy {

    @Override
    public List<IcaWorkflowStepConfig> getWorkflowSteps() {
        return List.of(
                IcaWorkflowStepConfig.builder()
                        .step(IcaWorkflowStep.CREATE)
                        .enabled(true)
                        .requiresCloPlo(true)
                        .creationMode("SINGLE")
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
                        .roundingMode("NONE")
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
                        .queryTimelineDays(14)
                        .build(),
                IcaWorkflowStepConfig.builder()
                        .step(IcaWorkflowStep.CONFIRM_SAP)
                        .enabled(true)
                        .confirmMode("COMPONENT_WISE")
                        .build()
        );
    }
}
