package com.nmims.lms.services.Ica;

import com.nmims.lms.Exceptions.CustomValidationException;
import com.nmims.lms.beans.Ica.Ica;
import com.nmims.lms.beans.Ica.IcaCloPloMaster;
import com.nmims.lms.campus.CampusStrategy;
import com.nmims.lms.campus.CampusStrategyFactory;
import com.nmims.lms.config.CampusConfig;
import com.nmims.lms.dto.Ica.IcaCloPloMasterDto;
import com.nmims.lms.dto.Ica.IcaRequest;
import com.nmims.lms.mapper.ica.IcaCloPloMasterMapper;
import com.nmims.lms.repositories.IcaCloPloMasterRepository;
import com.nmims.lms.repositories.IcaComponentRepository;
import com.nmims.lms.utilities.ResponseStructure;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("icaCloPloMasterService")
@Slf4j
@RequiredArgsConstructor
public class IcaCloPloMasterService {

    private final IcaCloPloMasterRepository repository;
    private final CampusStrategyFactory strategyFactory;
    private final IcaCloPloMasterMapper mapper;
    @Value("${school}")
    private String school;

    @Value("${campusCode}")
    private String campusCode;

    private CampusStrategy strategy;
    private CampusConfig config;

    public IcaCloPloMasterDto getICaCloPloMaster(IcaCloPloMaster icaCloPloMaster)
    {
        return mapper.toDto(icaCloPloMaster);
    }



    @Transactional
    public void init() {
        log.info("Inside the init() ");
        strategy = strategyFactory.getStrategy(campusCode, school);
        config = strategyFactory.getConfig(campusCode,school);
        log.info("Campus Strategy {}", config.getSupportsCloPlo());
    }

    public ResponseEntity<?> createIcaCloPloMaster(IcaCloPloMasterDto request) {
        if (strategy == null || config == null) {
            init();
        }
        if("Y".equals(config.getSupportsCloPlo())) {
            try {


                IcaCloPloMaster icaCloPloMaster = mapRequestToEntity(request);
                log.info("IcaCloPloMaster Details {} ", String.valueOf(icaCloPloMaster));
                IcaCloPloMaster saveEntity = repository.save(icaCloPloMaster);
                ResponseStructure<String> response = ResponseStructure.<String>builder().message("Successfully Clo-Plo Created! ").statusCode(201).data(saveEntity.getName()).build();
                return new ResponseEntity<>(response
                        , HttpStatus.OK
                );


            } catch (Exception ex) {
                log.info("IcaCloPloMaster Exception {} ", ex.getMessage());
                if (ex.getMessage() != null && ex.getMessage().contains("unique_name")) {
                    throw new CustomValidationException("Already exists for this name", HttpStatus.CONFLICT);
                }
                throw new CustomValidationException(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }else
        {
            return new ResponseEntity<>("This feature is not available for this campus",HttpStatus.UNAUTHORIZED);
        }
    }
    public IcaCloPloMaster mapRequestToEntity(IcaCloPloMasterDto request)
    {
        log.info("IcaCloPloMaster DTO {} ",request);
        IcaCloPloMaster icaCloPloMaster = new IcaCloPloMaster();
        icaCloPloMaster.setName(request.getName());
        icaCloPloMaster.setType(request.getType());
        icaCloPloMaster.setDescription(request.getDescription());
        icaCloPloMaster.setParentId(request.getParentId());

        return icaCloPloMaster;
    }


    public List<IcaCloPloMasterDto> findByTypeAndParentIsNull(String type) {

        return  repository.findByTypeAndParentIsNull(type).stream().map(mapper::toDto).collect(Collectors.toList());

    }

    public List<IcaCloPloMasterDto> findByParentIsNull() {

        List<IcaCloPloMaster> entities =
                repository.findAll();

        return entities.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

    }
}
