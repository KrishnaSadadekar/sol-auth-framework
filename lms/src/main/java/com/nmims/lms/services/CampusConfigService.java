package com.nmims.lms.services;

import com.nmims.lms.Exceptions.CustomValidationException;
import com.nmims.lms.Exceptions.ResourceNotFoundException;
import com.nmims.lms.config.CampusConfig;
import com.nmims.lms.repositories.CampusConfigRepository;
import com.nmims.lms.utilities.ResponseStructure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampusConfigService {

    private final CampusConfigRepository campusConfigRepository;

    public ResponseEntity<?> addCampusConfig(CampusConfig campusConfig) {
        try
        {   log.info("Campus Config : "+ campusConfig);
            CampusConfig saveCampusConfig=  campusConfigRepository.save(campusConfig);
            return new ResponseEntity<>(ResponseStructure.<String>builder().message("Successfully Added! ").statusCode(HttpStatus.CREATED.value()).build(), HttpStatus.OK);
        }catch (Exception ex)
        {   log.info("CampusConfig "+ex.getMessage());
            if (ex.getMessage() != null && ex.getMessage().contains("unique_campus_school")) {
                throw new CustomValidationException("Campus Config already exists for this School and Campus",HttpStatus.CONFLICT);
            }
            throw new CustomValidationException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> getAllCampusConfig(){
       List<CampusConfig> campusConfigList= campusConfigRepository.findAll();
        return new ResponseEntity<>(campusConfigList,HttpStatus.OK);
    }

    public ResponseEntity<?> getCampusConfig(Long id) {
        CampusConfig campusConfig = campusConfigRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CampusConfig not found with id: " + id));

        return new ResponseEntity<>(campusConfig, HttpStatus.OK);
    }


}
