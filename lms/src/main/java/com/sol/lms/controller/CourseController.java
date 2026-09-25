package com.sol.lms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sol.lms.entity.Course;
import com.sol.lms.repository.CourseRepository;

import sol.auth.client.principal.AuthUserPrincipal;
import sol.auth.client.sdk.UserManagementClient;
import sol.auth.client.tenant.TenantContext;
import sol.auth.common.dto.CommunicationResponse;
import sol.auth.common.dto.EmailRequest;

@RestController
public class CourseController {

    private final CourseRepository courseRepository;
    private final UserManagementClient userManagementClient;

    public CourseController(CourseRepository courseRepository, UserManagementClient userManagementClient) {
        this.courseRepository = courseRepository;
        this.userManagementClient = userManagementClient;
    }

    @GetMapping("/public/info")
    public ResponseEntity<Map<String, String>> publicInfo() {
        return ResponseEntity.ok(Map.of("service", "LMS Service", "status", "UP"));
    }

    @GetMapping("/api/courses")
    public ResponseEntity<List<Course>> getCourses(@AuthenticationPrincipal AuthUserPrincipal principal) {
        Long tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            return ResponseEntity.ok(courseRepository.findByTenantId(tenantId));
        }
        return ResponseEntity.ok(courseRepository.findAll());
    }

    @PostMapping("/api/courses/enroll")
    public ResponseEntity<Map<String, Object>> enroll(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @RequestBody Map<String, String> request) {

        String courseTitle = request.getOrDefault("courseTitle", "General Course");

        // Trigger email through centralized User Management server (no SMTP credentials in LMS!)
        CommunicationResponse emailResult = userManagementClient.sendEmail(EmailRequest.builder()
                .to(principal.getUsername())
                .subject("Enrollment Confirmation: " + courseTitle)
                .body("Hello " + principal.getUsername() + ", you have successfully enrolled in " + courseTitle)
                .html(false)
                .build());

        return ResponseEntity.ok(Map.of(
                "status", "ENROLLED",
                "user", principal.getUsername(),
                "tenantId", String.valueOf(principal.getTenantId()),
                "emailNotified", emailResult != null && emailResult.isSuccess()
        ));
    }
}
