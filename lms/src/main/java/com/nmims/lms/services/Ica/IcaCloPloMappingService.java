package com.nmims.lms.services.Ica;

import com.nmims.lms.beans.Ica.Ica;
import com.nmims.lms.beans.Ica.IcaCloPloMapping;
import com.nmims.lms.daos.BaseDAO;
import com.nmims.lms.dto.Ica.IcaCloPloMappingDto;
import com.nmims.lms.repositories.IcaCloPloRepository;
import com.nmims.lms.services.BaseService;
import com.nmims.lms.utilities.EntitySyncUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service("icaCloPloMappingService")
@Slf4j
@RequiredArgsConstructor
public class IcaCloPloMappingService  extends BaseService<IcaCloPloMapping> {
    private final IcaCloPloRepository repository;
    @Override
    protected BaseDAO<IcaCloPloMapping> getDAO() {
        return null;
    }


    @Transactional
    public void syncCloPloForComponent(
            Ica icaRef,
            Long componentId,
            List<IcaCloPloMappingDto> frontendMappings,
            Date now) {

        if (frontendMappings == null || frontendMappings.isEmpty()) {
            return;
        }

        /* 1. Fetch backend mappings */
        List<IcaCloPloMapping> backendMappings =
                repository.findByIcaIdAndComponentId(
                        icaRef.getId(),
                        componentId
                );

        /* 2. Generic sync */
        EntitySyncUtil.sync(
                frontendMappings,
                backendMappings,

                // Composite key
                dto -> dto.getCloId() + "_" + dto.getPloId(),
                entity -> entity.getCloId() + "_" + entity.getPloId(),

                // UPDATE
                (dto, entity) -> {
                    entity.setIfNotNull(dto.getMarks(), entity::setMarks);
                    entity.setActive("Y");
                    entity.setLastModifiedDate(now);
                },

                // CREATE
                dto -> {
                    IcaCloPloMapping entity = new IcaCloPloMapping();
                    entity.setIca(icaRef);
                    entity.setComponentId(componentId);
                    entity.setCloId(dto.getCloId());
                    entity.setPloId(dto.getPloId());
                    entity.setIfNotNull(dto.getMarks(), entity::setMarks);
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
                repository::saveAll
        );
    }

}
