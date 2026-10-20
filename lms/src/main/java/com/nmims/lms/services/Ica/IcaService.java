package com.nmims.lms.services.Ica;

import aj.org.objectweb.asm.commons.Remapper;
import com.nmims.lms.Exceptions.CustomValidationException;
import com.nmims.lms.Exceptions.ResourceNotFoundException;
import com.nmims.lms.beans.Ica.Ica;
import com.nmims.lms.beans.Ica.IcaComponent;
import com.nmims.lms.campus.CampusStrategy;
import com.nmims.lms.campus.CampusStrategyFactory;
import com.nmims.lms.config.CampusConfig;
import com.nmims.lms.daos.BaseDAO;
import com.nmims.lms.daos.ica.IcaDAO;
import com.nmims.lms.dto.Ica.IcaComponentDto;
import com.nmims.lms.dto.Ica.IcaRequest;
import com.nmims.lms.mapper.ica.IcaMapper;
import com.nmims.lms.repositories.IcaRepository;
import com.nmims.lms.services.BaseService;
import com.nmims.lms.utilities.ResponseStructure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("IcaService")
@RequiredArgsConstructor
@Slf4j
public class IcaService extends BaseService<Ica> {

    private final CampusStrategyFactory strategyFactory;
    private final IcaRepository icaRepository;
    private  final IcaDAO icaDAO;
    private  final IcaMapper mapper;

    @Override
    protected BaseDAO<Ica> getDAO() {
        return icaDAO;
    }


    public ResponseStructure<IcaRequest> createIca(IcaRequest icaRequest) {
        try {
            Ica ica = mapper.toEntity(icaRequest);
            log.info("Creating ICA with details: {}", ica); 
            Ica savedIca = icaRepository.save(ica);
            IcaRequest response = mapper.toDto(savedIca);
            response.setIcaId(savedIca.getId());
            return ResponseStructure.<IcaRequest>builder()
                    .message("Successfully Ica Created!")
                    .statusCode(201)
                    .data(response)
                    .build();

        } catch (Exception ex) {
            log.info("Error creating ICA: {}", ex.getMessage());
            if (ex.getMessage() != null && ex.getMessage().contains("unique_module_acad_campus_session_program")) {
                throw new CustomValidationException("ICA already exists for this Module, Year, Campus, Session, and Program.",HttpStatus.CONFLICT);
            }
            throw new CustomValidationException("ICA could not be created. Please try again.", HttpStatus.BAD_REQUEST);
        }
    }


    public void updateFinalSubmit(String finalSubmit, Long icaId) {
        icaRepository.updateFinalSubmit(finalSubmit,icaId);
    }

    public Optional<Ica> findById(Long icaId) {
        return  icaRepository.findById(icaId);
    }

    public Ica save(Ica ica) {
        return icaRepository.save(ica);
    }

    public IcaRequest findByIcaWithComponents(Long id) {

        return icaDAO.findByIcaWithComponents(id);

    }


}


