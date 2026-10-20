package com.nmims.lms.workflow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Resolves effective workflow step config by merging:
 *   1. Campus-specific DB rules (highest priority)
 *   2. School-level DB rules
 *   3. Java strategy defaults (lowest priority / fallback)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WorkflowRuleEvaluator {

    private final WorkflowRuleRepository ruleRepo;
    private final IcaWorkflowFactory workflowFactory;

    /**
     * Resolves the effective workflow steps for a campus/school combination.
     */
    public List<IcaWorkflowStepConfig> resolveEffectiveSteps(String campusCode, String school) {
        // 1. Get strategy fallback defaults
        List<IcaWorkflowStepConfig> strategyDefaults =
                workflowFactory.getWorkflowSteps(campusCode, school);

        // 2. Load school-level DB rules
        List<WorkflowRule> schoolRules =
                ruleRepo.findBySchoolAndCampusCodeIsNullAndActiveOrderByStepAsc(school, "Y");

        // 3. Load campus-specific DB rules
        List<WorkflowRule> campusRules =
                ruleRepo.findBySchoolAndCampusCodeAndActiveOrderByStepAsc(school, campusCode, "Y");

        // If no DB rules exist at all, return strategy defaults directly
        if (schoolRules.isEmpty() && campusRules.isEmpty()) {
            log.debug("No DB workflow rules for school={}, campus={}. Using strategy defaults.",
                    school, campusCode);
            return strategyDefaults;
        }

        // Index DB rules by step for fast lookup
        Map<IcaWorkflowStep, WorkflowRule> schoolRuleMap = schoolRules.stream()
                .collect(Collectors.toMap(WorkflowRule::getStep, Function.identity(), (a, b) -> b));

        Map<IcaWorkflowStep, WorkflowRule> campusRuleMap = campusRules.stream()
                .collect(Collectors.toMap(WorkflowRule::getStep, Function.identity(), (a, b) -> b));

        // 4. Merge: for each strategy step, overlay DB values
        return strategyDefaults.stream()
                .map(defaultCfg -> {
                    WorkflowRule schoolRule = schoolRuleMap.get(defaultCfg.getStep());
                    WorkflowRule campusRule = campusRuleMap.get(defaultCfg.getStep());
                    return merge(defaultCfg, schoolRule, campusRule);
                })
                .collect(Collectors.toList());
    }

    /**
     * Merges a strategy default with school-level and campus-level DB overrides.
     * Campus-level wins over school-level, which wins over strategy default.
     */
    private IcaWorkflowStepConfig merge(IcaWorkflowStepConfig base,
                                         WorkflowRule schoolRule,
                                         WorkflowRule campusRule) {
        // Start with strategy defaults
        IcaWorkflowStepConfig.IcaWorkflowStepConfigBuilder builder = IcaWorkflowStepConfig.builder()
                .step(base.getStep())
                .enabled(base.isEnabled())
                .requiresCloPlo(base.isRequiresCloPlo())
                .creationMode(base.getCreationMode())
                .componentWithCloPlo(base.isComponentWithCloPlo())
                .roundingMode(base.getRoundingMode())
                .marksInputType(base.getMarksInputType())
                .publishByAdmin(base.isPublishByAdmin())
                .queryTimelineDays(base.getQueryTimelineDays())
                .confirmMode(base.getConfirmMode());

        // Apply school-level overrides
        if (schoolRule != null) {
            applyOverrides(builder, schoolRule);
        }

        // Apply campus-level overrides (highest priority)
        if (campusRule != null) {
            applyOverrides(builder, campusRule);
        }

        return builder.build();
    }

    private void applyOverrides(IcaWorkflowStepConfig.IcaWorkflowStepConfigBuilder builder,
                                WorkflowRule rule) {
        if (rule.getEnabled() != null) {
            builder.enabled("Y".equalsIgnoreCase(rule.getEnabled()));
        }
        if (rule.getCreationMode() != null) {
            builder.creationMode(rule.getCreationMode());
        }
        if (rule.getRequiresCloPlo() != null) {
            builder.requiresCloPlo("Y".equalsIgnoreCase(rule.getRequiresCloPlo()));
        }
        if (rule.getComponentWithCloPlo() != null) {
            builder.componentWithCloPlo("Y".equalsIgnoreCase(rule.getComponentWithCloPlo()));
        }
        if (rule.getRoundingMode() != null) {
            builder.roundingMode(rule.getRoundingMode());
        }
        if (rule.getMarksInputType() != null) {
            builder.marksInputType(rule.getMarksInputType());
        }
        if (rule.getPublishByAdmin() != null) {
            builder.publishByAdmin("Y".equalsIgnoreCase(rule.getPublishByAdmin()));
        }
        if (rule.getQueryTimelineDays() != null) {
            builder.queryTimelineDays(rule.getQueryTimelineDays());
        }
        if (rule.getConfirmMode() != null) {
            builder.confirmMode(rule.getConfirmMode());
        }
    }
}
