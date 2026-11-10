package com.nmims.lms.controllers;

import com.nmims.lms.dto.Ica.BulkIcaRequest;
import com.nmims.lms.dto.Ica.BulkIcaResponse;
import com.nmims.lms.dto.Ica.IcaCloPloMasterDto;
import com.nmims.lms.dto.Ica.IcaComponentDto;
import com.nmims.lms.dto.Ica.IcaRequest;
import com.nmims.lms.dto.Ica.IcaTotalMarksDto;
import com.nmims.lms.services.Ica.IcaCloPloMasterService;
import com.nmims.lms.services.Ica.IcaComponentService;
import com.nmims.lms.services.Ica.IcaMarksService;
import com.nmims.lms.services.Ica.IcaOrchestrationService;
import com.nmims.lms.services.Ica.IcaService;
import com.nmims.lms.utilities.ResponseStructure;
import com.nmims.lms.workflow.IcaWorkflowFactory;
import com.nmims.lms.workflow.IcaWorkflowStepConfig;
import com.nmims.lms.workflow.WorkflowRuleEvaluator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ica")
@Slf4j
public class IcaController {

    private final IcaService icaService;
    private final IcaComponentService icaComponentService;
    private final IcaCloPloMasterService icaCloPloMasterService;
    private final IcaMarksService icaMarksService;
    private final IcaWorkflowFactory workflowFactory;
    private final WorkflowRuleEvaluator workflowRuleEvaluator;
    private final IcaOrchestrationService icaOrchestrationService;

    @PostMapping("/createIca")
    public ResponseEntity<?> createIca(@RequestBody IcaRequest icaRequest) {
        ResponseStructure<IcaRequest> response = icaService.createIca(icaRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/createIca/bulk")
    public ResponseEntity<?> createIcaBulk(@RequestBody BulkIcaRequest bulkRequest) {
        BulkIcaResponse response = icaOrchestrationService.createBulk(bulkRequest);
        return new ResponseEntity<>(
                ResponseStructure.builder()
                        .statusCode(201)
                        .message("Bulk ICA creation completed")
                        .data(response)
                        .build(),
                HttpStatus.CREATED);
    }

    @PostMapping("/addIcaComponent")
    public ResponseEntity<?> addIcaComponent(@RequestBody List<IcaComponentDto> request) {
        return icaComponentService.addIcaComponent(request);
    }

    @PostMapping("/createCloPlo")
    public ResponseEntity<?> createIcaCloPloMaster(@Valid @RequestBody IcaCloPloMasterDto request) {
        return icaCloPloMasterService.createIcaCloPloMaster(request);
    }

    @PostMapping(value = "/createIcaComponent", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createIcaComponent(@RequestBody IcaComponentDto request) {
        return icaComponentService.createIcaComponent(request);
    }

    @PostMapping(value = "/evaluateIca/{id}")
    public ResponseEntity<?> addIcaMarks(@PathVariable("id") Long id, @RequestBody List<IcaTotalMarksDto> icaMarksDto) {
        return icaMarksService.addIcaMarks(icaMarksDto, id);

    }

    @GetMapping(value = "/clo-plo")
    public ResponseEntity<?> getCloPloList() {
        return ResponseEntity.ok(icaCloPloMasterService.findByParentIsNull());
    }

    @GetMapping(value = "/components")
    public ResponseEntity<?> findAllComponents() {
        return ResponseEntity.ok(icaComponentService.findAllComponents());
    }

    @GetMapping("/{icaId}/components")
    public ResponseEntity<?> findComponentsByIca(@PathVariable("icaId") Long icaId) {
        return ResponseEntity.ok(icaComponentService.findByIcaId(icaId));
    }

    @GetMapping(value = "/clo-list")
    public ResponseEntity<?> getCloList() {
        return ResponseEntity.ok(icaCloPloMasterService.findByTypeAndParentIsNull("CLO"));
    }

    @GetMapping(value = "/plo-list")
    public ResponseEntity<?> getPloList() {
        return ResponseEntity.ok(icaCloPloMasterService.findByTypeAndParentIsNull("PLO"));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {

        return ResponseEntity.ok(icaService.findByIcaWithComponents(id));

    }

    @GetMapping(value = "/workflow-steps")
    public ResponseEntity<?> getWorkflowSteps(
            @RequestParam String campusCode,
            @RequestParam String school) {
        log.info("Received request for workflow steps with campusCode: {} and school: {}", campusCode, school);
        List<IcaWorkflowStepConfig> steps = workflowRuleEvaluator.resolveEffectiveSteps(campusCode, school);
        return ResponseEntity.ok(
                ResponseStructure.builder()
                        .statusCode(200)
                        .message("Workflow steps retrieved")
                        .data(steps)
                        .build());
    }

    @GetMapping(value = "/test")
    public ResponseEntity<?> test() {

        return ResponseEntity.ok("Its working! ");

    }

}
