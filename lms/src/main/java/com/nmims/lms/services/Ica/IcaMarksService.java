package com.nmims.lms.services.Ica;

import com.nmims.lms.Exceptions.CustomValidationException;
import com.nmims.lms.Exceptions.ResourceNotFoundException;
import com.nmims.lms.beans.Ica.*;
import com.nmims.lms.daos.BaseDAO;
import com.nmims.lms.dto.Ica.BaseIcaMarksDto;
import com.nmims.lms.dto.Ica.IcaComponentMarksDto;
import com.nmims.lms.dto.Ica.IcaTotalMarksDto;
import com.nmims.lms.services.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("icaMarksService")
@Slf4j
@RequiredArgsConstructor
public class IcaMarksService extends BaseService {


    private final IcaComponentMarksService icaComponentMarksService;
    private final IcaCloPloMarksService icaCloPloMarksService;
    private final IcaQueriesService icaQueriesService;
    private final IcaTotalMarksService icaTotalMarksService;
    private final IcaService icaService;
    private final IcaStudentBatchwiseService icaStudentBatchwiseService;

    @Override
    protected BaseDAO getDAO() {
        return null;
    }


    //    Save Ica Marks
    public ResponseEntity<?> addIcaMarks(List<IcaTotalMarksDto> icaTotalMarksDto, Long icaId) {

        try {
            if (strategy == null || config == null) {
                init();
            }

            List<IcaTotalMarks> icaTotalMarksList = icaTotalMarksDto.stream()
                    .map(this::mapToIcaTotalMarks)
                    .toList();

            for (IcaTotalMarks total : icaTotalMarksList) {

                if (total.getIca().getScaledMarks() != null) {
                    Double scaleMarks = getScaleMarks(total.getMarks(), total.getIca().getInternalMarks(), total.getIca().getScaledMarks());
                    total.setIfNotNull(scaleMarks, total::setIcaScaledMarks);
                }
                total = icaTotalMarksService.save(total);
                if (total.getIcaComponentMarks() != null && !total.getIcaComponentMarks().isEmpty()) {
                    for (IcaComponentMarks comp : total.getIcaComponentMarks()) {
                        comp = icaComponentMarksService.save(comp);
                        if (comp.getIcaCloPloMarks() != null && !comp.getIcaCloPloMarks().isEmpty() && "Y".equalsIgnoreCase(config.getSupportsCloPlo())) {
                            log.info("Inside the save Clo Plo marks ");
                            icaCloPloMarksService.saveAll(comp.getIcaCloPloMarks());
                        }

                    }
                }
                updateFinalSubmit(icaId);
                updateReEvaluatedStatus(icaId);
                return ResponseEntity.ok("ICA Marks inserted successfully for " + icaTotalMarksList.size() + " students.");
            }
            return null;
        } catch (Exception ex) {
            log.info("addIcaComponentMarks exception {}", ex.getMessage());
            throw new CustomValidationException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    private void updateFinalSubmit(Long icaId) {
        int batchCount = icaTotalMarksService.getBatchWiseStudentCount(icaId);
        int finalSubmittedStudents = icaTotalMarksService.getFinalSubmittedStudentCount(icaId);
        if (batchCount < 1 || finalSubmittedStudents == batchCount) {
            icaService.updateFinalSubmit("Y", icaId);
        }

    }

    private Double getScaleMarks(Double studentMarks, Long originalTotal, Double requiredTotal) {
        if (originalTotal <= 0) {
            return 0.0;
        }
        return (studentMarks * requiredTotal) / originalTotal;
    }

    private <T extends BaseIcaMarks> T setIcaMarks(T entity, BaseIcaMarksDto dto) {
        if (dto.getIcaId() != null) {
            Ica icaRef = entityManager.getReference(Ica.class, dto.getIcaId());
            entity.setIca(icaRef);
        }

        entity.setIfNotNull(dto.getIsAbsent(), entity::setIsAbsent);
        entity.setIfNotNull(dto.getFinalSubmit(), entity::setFinalSubmit);
        entity.setIfNotNull(dto.getIsQueryApproved(), entity::setIsQueryApproved);
        entity.setIfNotNull(dto.getQuery(), entity::setQuery);
        entity.setIfNotNull(dto.getIsQueryRaise(), entity::setIsQueryRaise);
        entity.setIfNotNull(dto.getRemarks(), entity::setRemarks);
        entity.setIfNotNull(dto.getSaveAsDraft(), entity::setSaveAsDraft);
        entity.setIfNotNull(dto.getUsername(), entity::setUsername);
        entity.setIfNotNull(dto.getMarks(), entity::setMarks);
        entity.setIfNotNull(dto.getIsQueryApproved(), entity::setIsQueryApproved);

        return entity;
    }

    public IcaTotalMarks mapToIcaTotalMarks(IcaTotalMarksDto totalDto) {

        IcaTotalMarks total = setIcaMarks(new IcaTotalMarks(), totalDto);
        log.info("Strategy {} ", strategy);
        log.info("total DTO {}", totalDto);

        if (totalDto.getIcaComponentMarksDtos() != null) {
            List<IcaComponentMarks> componentMarksList = totalDto.getIcaComponentMarksDtos().stream()
                    .map(compDto -> {
                        IcaComponentMarks comp = setIcaMarks(new IcaComponentMarks(), compDto);
                        comp.setIfNotNull(compDto.getComponentId(), comp::setComponentId);
                        comp.setIfNotNull(total.getUsername(), comp::setUsername);
                        comp.setIfNotNull(total.getIca(), comp::setIca);
                        comp.setIfNotNull(total.getFinalSubmit(), comp::setFinalSubmit);
                        comp.setIfNotNull(total.getIsAbsent(),comp::setIsAbsent);
                        // handle cloPloMarks (optional)
                        log.info("Support Clo Plo {} ", config.getSupportsCloPlo());
                        log.info("get clo Plo  {} ", compDto.getIcaCloPloMarksDtos());
                        if (compDto.getIcaCloPloMarksDtos() != null && "Y".equalsIgnoreCase(config.getSupportsCloPlo())) {
                            List<IcaCloPloMarks> cloPloMarksList = compDto.getIcaCloPloMarksDtos().stream()
                                    .map(cloPloDto -> {
                                        IcaCloPloMarks cloPlo = setIcaMarks(new IcaCloPloMarks(), cloPloDto);
                                        cloPlo.setIfNotNull(cloPloDto.getCloId(), cloPlo::setCloId);
                                        cloPlo.setIfNotNull(cloPloDto.getPloId(), cloPlo::setPloId);
                                        cloPlo.setIfNotNull(total.getUsername(), cloPlo::setUsername);
                                        cloPlo.setIfNotNull(total.getIca(), cloPlo::setIca);
                                        cloPlo.setIfNotNull(compDto.getComponentId(), cloPlo::setComponentId);
                                        cloPlo.setIfNotNull(total.getFinalSubmit(), cloPlo::setFinalSubmit);
                                        cloPlo.setIfNotNull(total.getIsAbsent(),cloPlo::setIsAbsent);
                                        return cloPlo;
                                    })
                                    .collect(Collectors.toList());
                            comp.setIcaCloPloMarks(cloPloMarksList);
                        }

                        return comp;
                    })
                    .collect(Collectors.toList());

            total.setIcaComponentMarks(componentMarksList);
        }
        log.info("Returning Ica Total Marks Object {}", total);
        return total;
    }


    public void updateReEvaluatedStatus(Long icaId) {
        List<IcaQueries> queryRaisedStudents = icaQueriesService.findByIcaIdAndIsReEvaluatedAndIsApproved(icaId, "Y", "Y");
        if (!queryRaisedStudents.isEmpty()) {
            queryRaisedStudents.forEach(x -> x.setIsReEvaluated("Y"));
            icaQueriesService.saveAll(queryRaisedStudents);
        }

    }


    public ResponseEntity<?> addIcaMarksComponentWise(List<IcaComponentMarksDto> icaComponentMarksDto, Long icaId, Long componentId, String username) {
        try {
            if (strategy == null || config == null) {
                init();
            }

            List<IcaComponentMarks> icaComponentMarksList = icaComponentMarksDto.stream()
                    .map(this::mapToIcaComponentMarks)
                    .toList();

            for (IcaComponentMarks comp : icaComponentMarksList) {
                comp = icaComponentMarksService.save(comp);
                if (comp.getIcaCloPloMarks() != null && !comp.getIcaCloPloMarks().isEmpty() && "Y".equalsIgnoreCase(config.getSupportsCloPlo())) {
                    log.info("Inside the save Clo Plo marks ");
                    icaCloPloMarksService.saveAll(comp.getIcaCloPloMarks());
                }

            }
            processFinalSubmissionOfIca(icaId, componentId, username);
//            Send Email Notification
            return ResponseEntity.ok("ICA Marks inserted successfully for " + icaComponentMarksList.size() + " students.");

        } catch (Exception ex) {
            log.info("addIcaComponentMarks exception {}", ex.getMessage());
            throw new CustomValidationException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    private IcaComponentMarks mapToIcaComponentMarks(IcaComponentMarksDto icaComponentMarksDto) {

        IcaComponentMarks comp = setIcaMarks(new IcaComponentMarks(), icaComponentMarksDto);


        // handle cloPloMarks (optional)
        log.info("Support Clo Plo {} ", config.getSupportsCloPlo());
        log.info("get clo Plo  {} ", icaComponentMarksDto.getIcaCloPloMarksDtos());
        if (icaComponentMarksDto.getIcaCloPloMarksDtos() != null && "Y".equalsIgnoreCase(config.getSupportsCloPlo())) {
            List<IcaCloPloMarks> cloPloMarksList = icaComponentMarksDto.getIcaCloPloMarksDtos().stream()
                    .map(cloPloDto -> {
                        IcaCloPloMarks cloPlo = setIcaMarks(new IcaCloPloMarks(), cloPloDto);
                        cloPlo.setIfNotNull(cloPloDto.getCloId(), cloPlo::setCloId);
                        cloPlo.setIfNotNull(cloPloDto.getPloId(), cloPlo::setPloId);
                        cloPlo.setIfNotNull(comp.getUsername(), cloPlo::setUsername);
                        cloPlo.setIfNotNull(comp.getIca(), cloPlo::setIca);
                        cloPlo.setIfNotNull(comp.getComponentId(), cloPlo::setComponentId);


                        return cloPlo;
                    })
                    .collect(Collectors.toList());
            comp.setIcaCloPloMarks(cloPloMarksList);
        }

        return comp;

    }

    public void processFinalSubmissionOfIca(Long icaId, Long icaComponentId, String username) {
        List<IcaComponentMarks> icaComponentMarks = icaComponentMarksService.findByLastModifiedByAndIcaId(username, icaId);
        int batchwiseStudentCount = icaStudentBatchwiseService.countByIcaId(icaId);
        int totalMarksSubmittedStudents = icaTotalMarksService.getTotalMarksSubmittedCount(icaId);
        if (batchwiseStudentCount < 1 || batchwiseStudentCount == totalMarksSubmittedStudents) {
            Ica ica = icaService.findById(icaId)
                    .orElseThrow(() -> new ResourceNotFoundException("ICA not found"));

            ica.setIsSubmitted("Y");
            icaService.save(ica);

        }

    }

}
