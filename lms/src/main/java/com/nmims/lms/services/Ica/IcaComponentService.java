package com.nmims.lms.services.Ica;

import com.nmims.lms.Exceptions.CustomValidationException;
import com.nmims.lms.beans.Ica.Ica;
import com.nmims.lms.beans.Ica.IcaComponent;
import com.nmims.lms.daos.BaseDAO;
import com.nmims.lms.daos.ica.IcaComponentDAO;
import com.nmims.lms.dto.Ica.IcaComponentDto;
import com.nmims.lms.mapper.ica.IcaComponentMapper;
import com.nmims.lms.repositories.IcaComponentRepository;
import com.nmims.lms.services.BaseService;
import com.nmims.lms.utilities.EntitySyncUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service("icaComponentService")
@Slf4j
@RequiredArgsConstructor
public class IcaComponentService extends BaseService<IcaComponent> {

    private final IcaComponentRepository componentRepo;
    private final IcaCloPloMappingService icaCloPloMappingService;
    private final IcaComponentDAO icaComponentDAO;
    private final IcaComponentMapper mapper;
    private final IcaService icaService;

    @PersistenceContext
    private final EntityManager entityManager;


    @Override
    protected BaseDAO<IcaComponent> getDAO() {
        return icaComponentDAO;
    }


    public List<IcaComponentDto> getComponents(Long icaId) {
        if (strategy == null || config == null) {
            init();
        }
        return strategy.processComponents(icaId, config);
    }


    @Transactional
    public ResponseEntity<?> addIcaComponent(List<IcaComponentDto> componentDtos) {
        try {
            if (strategy == null || config == null) {
                init();
            }
            if (componentDtos == null || componentDtos.isEmpty()) {
                return new ResponseEntity<>("Empty ICA Component list", HttpStatus.BAD_REQUEST);
            }
            // All payload entries belong to same ICA
            Long icaId = componentDtos.get(0).getIcaId();
            log.info("All ICA Components added successfully");
            return syncIcaComponents(icaId,componentDtos);
        } catch (Exception ex) {
            log.error("Exception while adding ICA Components", ex);
            throw new CustomValidationException(ex.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }


    public ResponseEntity<?> createIcaComponent(IcaComponentDto componentDto) {
        try {
            init();
            log.info("ComponentDTO: {}", componentDto);

            IcaComponent icaComponent = new IcaComponent();
            icaComponent.setComponentName(componentDto.getComponentName());
            icaComponent.setActive("Y");

            log.info("icaComponent: {}", icaComponent);

            int isInserted = this.insert(icaComponent);

            if (isInserted > 0) {
                return new ResponseEntity<>("Successfully Created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Creation failed", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception ex) {
            log.error("Error creating ICA component", ex);
            throw new CustomValidationException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public List<IcaComponentDto> findAllComponents() {

        List<IcaComponent> componentList = icaComponentDAO.findAllComponents();
        return componentList.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<IcaComponentDto> findByIcaId(Long icaId) {
        if (strategy == null || config == null) {
            init();
        }
        List<IcaComponent> backendIcaComponent = icaComponentDAO.findAllComponents();
        return backendIcaComponent.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ResponseEntity<?> syncIcaComponents(
            Long icaId,
            List<IcaComponentDto> frontendComponents) {

        try {
            IntStream.range(0, frontendComponents.size())
                    .forEach(i -> frontendComponents.get(i).setSequenceNo(i + 1));
            Date now = new Date();
            var icaRef = entityManager.getReference(Ica.class, icaId);
            log.info("Syncing ICA Components for ICA Id {}", icaId);
            /* -------------------------------------------------
             * 1. FETCH BACKEND COMPONENTS
             * ------------------------------------------------- */
            List<IcaComponent> backendComponents =
                    componentRepo.findByIcaId(icaId);

            /* -------------------------------------------------
             * 2. SYNC ICA COMPONENTS
             * ------------------------------------------------- */
            EntitySyncUtil.sync(
                    frontendComponents,
                    backendComponents,

                    // Frontend key
                    IcaComponentDto::getIcaComponentId,

                    // Backend key
                    IcaComponent::getComponentId,

                    // UPDATE
                    (dto, entity) -> {
                        mapper.updateEntity(dto, entity);
                        entity.setLastModifiedDate(now);
                        entity.setActive("Y");
                    },

                    // CREATE
                    dto -> {
                        IcaComponent entity = mapper.toEntity(dto);
                        entity.setIca(icaRef);
                        entity.setIfNotNull(dto.getIcaComponentId(), entity::setComponentId);
                        entity.setCreatedDate(now);
                        entity.setActive("Y");

                        return entity;
                    },

                    // SOFT DELETE 
                    entity -> {
                        entity.setActive("N");
                        entity.setLastModifiedDate(now);
                    },

                    // SAVE
                    componentRepo::saveAll
            );
            componentRepo.flush();
            if ("Y".equalsIgnoreCase(config.getSupportsCloPlo())) { 

                for (IcaComponentDto frontend : frontendComponents) {

                    if (frontend.getIcaComponentId() == null ||
                            frontend.getIcaCloPloMappingDto() == null ||
                            frontend.getIcaCloPloMappingDto().isEmpty()) {
                        continue;
                    }

                    icaCloPloMappingService.syncCloPloForComponent(
                            icaRef,
                            frontend.getIcaComponentId(),
                            frontend.getIcaCloPloMappingDto(),
                            now
                    );
                }
            }

            log.info("ICA Components + CLO–PLO synced successfully");
            return ResponseEntity.ok("ICA Components synced successfully");

        } catch (Exception ex) {
            log.error("Error while syncing ICA Components", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while saving ICA Components");
        }
    }

}
