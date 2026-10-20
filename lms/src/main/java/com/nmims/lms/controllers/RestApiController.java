package com.nmims.lms.controllers;


import com.nmims.lms.beans.Course.UserCourse;
import com.nmims.lms.config.CampusConfig;
import com.nmims.lms.services.CampusConfigService;
import com.nmims.lms.services.CourseService;
import com.nmims.lms.services.Program.ProgramCampusService;
import com.nmims.lms.services.Program.ProgramService;
import com.nmims.lms.services.UserCourseService;
import com.nmims.lms.utilities.ResponseStructure;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestApiController {

    private final CampusConfigService campusConfigService;
    private final ProgramService programService;
    private final ProgramCampusService programCampusService;
    private final CourseService courseService;
    private final UserCourseService userCourseService;


    @PostMapping(value = "/addCampusConfig", consumes = "application/json")
    public ResponseEntity<?> addCampusConfig(@Valid @RequestBody CampusConfig campusConfig) {
        return campusConfigService.addCampusConfig(campusConfig);
    }

    @GetMapping(value = "/academic-years")
    public ResponseEntity<ResponseStructure<Object>> getAcademicYears() {

        ResponseStructure<Object> rs = ResponseStructure.builder().data(courseService.findAllAcademicYears()).statusCode(200).build();
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @GetMapping("/programs")
    public ResponseEntity<?> getPrograms() {
        ResponseStructure<Object> res =
                ResponseStructure.builder().data(programService.findByActive("Y"))
                        .statusCode(200).build();
        return ResponseEntity.ok(res);
    }

    @GetMapping(value = "programs", params = {"acadYear", "campusId"})
    public ResponseEntity<?> getProgramsByAcademicYearAndCampus(@RequestParam String acadYear, @RequestParam String campusId) {
        return ResponseEntity.ok(ResponseStructure.builder().data(programService.findByAcadYearAndCampusId(acadYear, campusId).stream())
                .statusCode(200).build());
    }

    @GetMapping(value = "programs", params = {"campusId"})
    public ResponseEntity<?> getProgramsByCampus(@RequestParam String campusId) {
        return ResponseEntity.ok(ResponseStructure.builder().data(programService.findByCampusId(campusId).stream())
                .statusCode(200).build());
    }


    @GetMapping(value = "programs", params = {"academicSession","academicYear","moduleId"})
    public ResponseEntity<?> getProgramsByAcademicSessionAndAcademicYearAndSubject(@RequestParam String academicSession, @RequestParam Long academicYear, @RequestParam Long moduleId) {
        return ResponseEntity.ok(ResponseStructure.builder().data(programService.findProgramsByAcademicSessionAndAcademicYearAndSubject(academicSession ,academicYear ,moduleId).stream())
                .statusCode(200).build());
    }

    @GetMapping(value = "/campuses")
    public ResponseEntity<?> getAllCampuses() {
        return ResponseEntity.ok(ResponseStructure.builder().data(programCampusService.findAllCampuses().stream())
                .statusCode(200).build());
    }

    @GetMapping(value = "/acad-sessions" ,params = {"academicYear", "campusId"})
    public ResponseEntity<?> findAcademicSessionsByAcademicYearAndCampus(@RequestParam Long academicYear, @RequestParam(required = false) Long campusId) {
        return ResponseEntity.ok(ResponseStructure.builder().data(courseService.findAcademicSessionsByAcademicYearAndCampus(academicYear,campusId).stream()).statusCode(200).build());
    }


    @GetMapping(value = "/subjects", params = {"academicYear", "campusId", "academicSession"})
    public ResponseEntity<?> findSubjectByAcademicSessionAndAcademicYearAndCampus(@RequestParam Long academicYear, @RequestParam(required = false) Long campusId ,@RequestParam String academicSession) {
        return ResponseEntity.ok(ResponseStructure.builder().data(courseService.findSubjectByAcademicSessionAndAcademicYearAndCampus(academicSession, academicYear,campusId).stream())
                .statusCode(200).build());
    }


    @GetMapping(value = "/faculties")
    public ResponseEntity<?> findAllFaculties() {
        return ResponseEntity.ok(ResponseStructure.builder().data(userCourseService.findAllFaculties())
                .statusCode(200).build());
    }

    @GetMapping(value = "/faculties", params = {"academicYear", "moduleId"})
        public ResponseEntity<?> findFacultiesBySubjectAndAcademicYear(@RequestParam Long academicYear, @RequestParam Long moduleId) {
       List<UserCourse> faculties= userCourseService.findFacultiesBySubjectAndAcademicYear(academicYear,moduleId );
       log.info("Faculties {}",faculties);
        return ResponseEntity.ok(ResponseStructure.builder().data(userCourseService.findFacultiesBySubjectAndAcademicYear(academicYear,moduleId ))
                .statusCode(200).build());
    }
}
