package com.nmims.lms.services.Ica;

import com.nmims.lms.beans.Ica.Ica;
import com.nmims.lms.dto.Ica.BulkIcaRequest;
import com.nmims.lms.dto.Ica.BulkIcaResponse;
import com.nmims.lms.dto.Ica.BulkIcaResponse.BulkIcaItemResult;
import com.nmims.lms.mapper.ica.IcaMapper;
import com.nmims.lms.repositories.IcaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Orchestrates bulk ICA creation: one ICA per module, shared common fields.
 * Handles duplicates gracefully and returns per-module status.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IcaOrchestrationService {

    private final IcaRepository icaRepository;
    private final IcaMapper mapper;

    @Transactional
    public BulkIcaResponse createBulk(BulkIcaRequest request) {
        List<BulkIcaItemResult> results = new ArrayList<>();

        for (String moduleId : request.getModuleIds()) {
            results.add(createSingleForModule(request, moduleId));
        }

        int created = (int) results.stream()
                .filter(r -> "CREATED".equals(r.getStatus())).count();
        int skipped = (int) results.stream()
                .filter(r -> "DUPLICATE".equals(r.getStatus())).count();
        int failed = (int) results.stream()
                .filter(r -> "FAILED".equals(r.getStatus())).count();

        return BulkIcaResponse.builder()
                .totalRequested(request.getModuleIds().size())
                .created(created)
                .skipped(skipped)
                .failed(failed)
                .results(results)
                .build();
    }

    private BulkIcaItemResult createSingleForModule(BulkIcaRequest request, String moduleId) {
        try {
            Ica ica = buildIcaFromBulk(request, moduleId);
            Ica saved = icaRepository.save(ica);
            log.info("Bulk ICA created: moduleId={}, icaId={}", moduleId, saved.getId());
            return BulkIcaItemResult.builder()
                    .moduleId(moduleId)
                    .status("CREATED")
                    .icaId(saved.getId())
                    .message("ICA created successfully")
                    .build();
        } catch (Exception ex) {
            if (ex.getMessage() != null
                    && ex.getMessage().contains("unique_module_acad_campus_session_program")) {
                log.info("Bulk ICA skipped (duplicate): moduleId={}", moduleId);
                return BulkIcaItemResult.builder()
                        .moduleId(moduleId)
                        .status("DUPLICATE")
                        .message("ICA already exists for this module/year/campus/session/program")
                        .build();
            }
            log.error("Bulk ICA failed: moduleId={}, error={}", moduleId, ex.getMessage());
            return BulkIcaItemResult.builder()
                    .moduleId(moduleId)
                    .status("FAILED")
                    .message("ICA could not be created")
                    .build();
        }
    }

    private Ica buildIcaFromBulk(BulkIcaRequest request, String moduleId) {
        Ica ica = new Ica();
        ica.setModuleId(moduleId);
        ica.setAcadYear(request.getAcadYear());
        ica.setAcadSession(request.getAcadSession());
        ica.setCampusId(request.getCampusId());
        ica.setProgramId(request.getProgramId());
        ica.setIcaName(request.getIcaName());
        ica.setIcaDesc(request.getDescription());
        ica.setAssignedFaculty(request.getAssignedFaculty());
        ica.setInternalMarks(request.getInternalMarks());
        ica.setExternalMarks(request.getExternalMarks());
        ica.setInternalPassMarks(request.getInternalPassMarks());
        ica.setExternalPassMarks(request.getExternalPassingMarks());
        ica.setTotalMarks(request.getTotalMarks());
        ica.setStartDate(request.getStartDate());
        ica.setEndDate(request.getEndDate());
        ica.setDivision(request.getDivision());
        ica.setIsCourseraIca(request.getIsCourseraIca());
        ica.setIsIcaDivisionWise(request.getIsIcaDivisionWise());
        ica.setIsPublishCompWise(request.getIsPublishedCompWise());
        ica.setIsNonEventModule(request.getIsNoEventModule());
        ica.setEventId(request.getEventId());
        return ica;
    }
}
