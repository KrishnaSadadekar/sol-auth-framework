package com.nmims.lms.campus;

import com.nmims.lms.config.CampusConfig;
import com.nmims.lms.repositories.CampusConfigRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CampusStrategyFactory {

    private final Map<String, CampusStrategy> strategies;
    private final CampusConfigRepository configRepo;

    public CampusStrategyFactory(Map<String, CampusStrategy> strategies,
                                 CampusConfigRepository configRepo) {
        this.strategies = strategies;
        this.configRepo = configRepo;
    }

    /**
     * Get the appropriate strategy based on campusCode and school.
     * Falls back to DEFAULT strategy if no special one found.
     */
    public CampusStrategy getStrategy(String campusCode, String school) {
        // Fetch config from DB
        CampusConfig config = configRepo.findByCampusCodeAndSchool(campusCode, school)
                .orElseGet(this::getDefaultConfig);

        // Pick the appropriate strategy bean
        String strategyKey = config.getSpecialStrategy() != null
                ? config.getSpecialStrategy()
                : "DEFAULT";

        return strategies.getOrDefault(strategyKey, strategies.get("DEFAULT"));
    }

    /**
     * Get CampusConfig (returns DEFAULT config if none found)
     */
    public CampusConfig getConfig(String campusCode, String school) {
        return configRepo.findByCampusCodeAndSchool(campusCode, school)
                .orElseGet(this::getDefaultConfig);
    }

    /**
     * Internal helper to safely fetch default config
     */
    private CampusConfig getDefaultConfig() {
        // Define what your "default" is — e.g., campusCode="DEFAULT", school="DEFAULT"
        return configRepo.findByCampusCodeAndSchool("DEFAULT", "DEFAULT")
                .orElseGet(() -> {
                    CampusConfig defaultConfig = new CampusConfig();
                    defaultConfig.setCampusCode("DEFAULT");
                    defaultConfig.setSchool("DEFAULT");
                    defaultConfig.setSupportsCloPlo("Y");
                    defaultConfig.setSpecialStrategy("DEFAULT");
                    return defaultConfig;
                });
    }
}
