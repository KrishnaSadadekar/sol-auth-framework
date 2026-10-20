package com.nmims.lms.workflow;

import com.nmims.lms.config.CampusConfig;
import com.nmims.lms.repositories.CampusConfigRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Factory that resolves the correct IcaWorkflowStrategy based on campus/school configuration.
 * Uses Spring's automatic bean name resolution via Map<String, IcaWorkflowStrategy>.
 */
@Component
public class IcaWorkflowFactory {

    private final Map<String, IcaWorkflowStrategy> strategies;
    private final CampusConfigRepository configRepo;

    public IcaWorkflowFactory(Map<String, IcaWorkflowStrategy> strategies,
                              CampusConfigRepository configRepo) {
        this.strategies = strategies;
        this.configRepo = configRepo;
    }

    /**
     * Get the workflow strategy for a given campus and school.
     * Falls back to DEFAULT_WORKFLOW if no specific strategy is configured.
     */
    public IcaWorkflowStrategy getWorkflowStrategy(String campusCode, String school) {
        CampusConfig config = configRepo.findByCampusCodeAndSchool(campusCode, school)
                .orElse(null);

        String strategyKey = "DEFAULT_WORKFLOW";
        if (config != null && config.getSpecialStrategy() != null) {
            strategyKey = config.getSpecialStrategy() + "_WORKFLOW";
        }

        return strategies.getOrDefault(strategyKey, strategies.get("DEFAULT_WORKFLOW"));
    }

    /**
     * Get workflow steps for a campus/school combination.
     */
    public List<IcaWorkflowStepConfig> getWorkflowSteps(String campusCode, String school) {
        return getWorkflowStrategy(campusCode, school).getWorkflowSteps();
    }
}
