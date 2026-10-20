package com.nmims.lms.workflow;

import java.util.List;

/**
 * Strategy interface for ICA workflow.
 * Each school/campus implements this to define its own workflow steps and behavior.
 * Open for extension (new strategies), closed for modification (interface is stable).
 */
public interface IcaWorkflowStrategy {

    /**
     * Returns the ordered list of workflow steps for this school.
     */
    List<IcaWorkflowStepConfig> getWorkflowSteps();

    /**
     * Returns whether a specific step is enabled for this school.
     */
    default boolean isStepEnabled(IcaWorkflowStep step) {
        return getWorkflowSteps().stream()
                .anyMatch(s -> s.getStep() == step && s.isEnabled());
    }

    /**
     * Returns the configuration for a specific step.
     */
    default IcaWorkflowStepConfig getStepConfig(IcaWorkflowStep step) {
        return getWorkflowSteps().stream()
                .filter(s -> s.getStep() == step)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the next enabled step after the given step, or null if last.
     */
    default IcaWorkflowStep getNextStep(IcaWorkflowStep currentStep) {
        List<IcaWorkflowStepConfig> steps = getWorkflowSteps();
        for (int i = 0; i < steps.size() - 1; i++) {
            if (steps.get(i).getStep() == currentStep && steps.get(i).isEnabled()) {
                for (int j = i + 1; j < steps.size(); j++) {
                    if (steps.get(j).isEnabled()) {
                        return steps.get(j).getStep();
                    }
                }
            }
        }
        return null;
    }

    // ─── Per-step convenience methods ───────────────────────

    /** Step 1: How ICAs are created — "SINGLE", "BULK", or "EXCEL_UPLOAD" */
    default String getCreationMode() {
        IcaWorkflowStepConfig cfg = getStepConfig(IcaWorkflowStep.CREATE);
        return cfg != null && cfg.getCreationMode() != null ? cfg.getCreationMode() : "SINGLE";
    }

    /** Step 2: Whether component mapping includes CLO/PLO */
    default boolean isComponentWithCloPlo() {
        IcaWorkflowStepConfig cfg = getStepConfig(IcaWorkflowStep.ADD_COMPONENT);
        return cfg != null && cfg.isComponentWithCloPlo();
    }

    /** Step 3: Rounding mode for marks — "ROUND", "FLOOR", "CEIL", "NONE" */
    default String getRoundingMode() {
        IcaWorkflowStepConfig cfg = getStepConfig(IcaWorkflowStep.EVALUATE);
        return cfg != null && cfg.getRoundingMode() != null ? cfg.getRoundingMode() : "ROUND";
    }

    /** Step 3: Marks input type — "INTEGER" or "DOUBLE" */
    default String getMarksInputType() {
        IcaWorkflowStepConfig cfg = getStepConfig(IcaWorkflowStep.EVALUATE);
        return cfg != null && cfg.getMarksInputType() != null ? cfg.getMarksInputType() : "INTEGER";
    }

    /** Step 5: Number of days allowed for query resolution */
    default int getQueryTimelineDays() {
        IcaWorkflowStepConfig cfg = getStepConfig(IcaWorkflowStep.RAISE_QUERY);
        return cfg != null && cfg.getQueryTimelineDays() != null ? cfg.getQueryTimelineDays() : 7;
    }

    /** Step 6: Confirm mode — "TOTAL_MARKS" or "COMPONENT_WISE" */
    default String getConfirmMode() {
        IcaWorkflowStepConfig cfg = getStepConfig(IcaWorkflowStep.CONFIRM_SAP);
        return cfg != null && cfg.getConfirmMode() != null ? cfg.getConfirmMode() : "TOTAL_MARKS";
    }
}
