package com.nmims.lms.campus;


import com.nmims.lms.beans.Ica.Ica;
import com.nmims.lms.beans.Ica.IcaComponent;
import com.nmims.lms.config.CampusConfig;
import com.nmims.lms.dto.Ica.IcaCloPloMappingDto;
import com.nmims.lms.dto.Ica.IcaComponentDto;
import com.nmims.lms.repositories.IcaComponentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Component("DEFAULT")
public class DefaultCampusStrategy implements CampusStrategy {

    private final IcaComponentRepository componentRepo;

    @PersistenceContext
    private EntityManager entityManager;
    public DefaultCampusStrategy(IcaComponentRepository componentRepo) {
        this.componentRepo = componentRepo;
    }

    @Override
    public String supportsCloPlo() {
        return "N";
    }

    @Override
    public List<IcaComponentDto> processComponents(Long icaId, CampusConfig config) {
        var icaref =entityManager.getReference(Ica.class, icaId);
        List<IcaComponent> components = componentRepo.findByIca(icaref);
        return components.stream().map(comp -> {
            IcaComponentDto dto = new IcaComponentDto();
            dto.setId(comp.getId());

            dto.setMarks(comp.getMarks());

            if ("Y".equalsIgnoreCase(config.getSupportsCloPlo())) {
                // Return with CLO/PLO mapping if present
                dto.setIcaCloPloMappingDto(
                        comp.getCloPloMappings() != null
                                ? comp.getCloPloMappings().stream()
                                .map(m -> new IcaCloPloMappingDto(
                                        m.getCloId(),
                                        m.getPloId(),
                                        m.getComponentId(),
                                        m.getMarks()
                                ))
                                .collect(Collectors.toList())
                                : Collections.emptyList()
                );
            } else {
                // CLO/PLO not supported for this campus/school
                dto.setIcaCloPloMappingDto(Collections.emptyList());
            }


            return dto;
        }).collect(Collectors.toList());

    }
}
