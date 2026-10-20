package com.nmims.lms.campus;

import com.nmims.lms.beans.Ica.Ica;
import com.nmims.lms.beans.Ica.IcaComponent;
import com.nmims.lms.config.CampusConfig;
import com.nmims.lms.dto.Ica.IcaCloPloMappingDto;
import com.nmims.lms.dto.Ica.IcaComponentDto;
import com.nmims.lms.repositories.IcaComponentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component("SBM-NM-M")
@RequiredArgsConstructor
public class SbmCampusStrategy implements CampusStrategy {
    private  final IcaComponentRepository componentRepo;
    @Override
    public String supportsCloPlo() {
        return "Y";
    }

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<IcaComponentDto> processComponents(Long icaId, CampusConfig config) {
        var icaref =entityManager.getReference(Ica.class, icaId);
        List<IcaComponent> components = componentRepo.findByIca(icaref);

        return components.stream().map(comp -> {
            IcaComponentDto dto = new IcaComponentDto();
            dto.setId(comp.getId());

            dto.setMarks(comp.getMarks());


            dto.setIcaCloPloMappingDto(comp.getCloPloMappings().stream()
                    .map(m -> new IcaCloPloMappingDto(
                            m.getCloId(),
                            m.getPloId(),
                            m.getComponentId(),
                            m.getMarks()

                    ))
                    .collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

}
