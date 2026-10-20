package com.nmims.lms.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nmims.lms.services.UserService;
import com.nmims.lms.utilities.ResponseStructure;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sol.auth.client.principal.AuthUserPrincipal;
import sol.auth.common.dto.CommunicationResponse;
import sol.auth.common.dto.EmailRequest;
import sol.auth.common.dto.SmsRequest;
import sol.auth.common.dto.UserSummaryResponse;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ResponseStructure<AuthUserPrincipal>> getCurrentUser(
            @AuthenticationPrincipal AuthUserPrincipal principal) {
        log.info("Fetching authenticated user context: {}", principal != null ? principal.getUsername() : "anonymous");
        return ResponseEntity.ok(
                ResponseStructure.<AuthUserPrincipal>builder()
                        .statusCode(200)
                        .message("Authenticated user retrieved successfully")
                        .data(principal)
                        .build()
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<ResponseStructure<UserSummaryResponse>> getCurrentUserProfile() {
        UserSummaryResponse profile = userService.getCurrentUserProfile();
        return ResponseEntity.ok(
                ResponseStructure.<UserSummaryResponse>builder()
                        .statusCode(200)
                        .message("User profile retrieved from Auth Server")
                        .data(profile)
                        .build()
        );
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ResponseStructure<UserSummaryResponse>> getUserProfileById(@PathVariable("userId") Long userId) {
        UserSummaryResponse profile = userService.getUserProfileById(userId);
        return ResponseEntity.ok(
                ResponseStructure.<UserSummaryResponse>builder()
                        .statusCode(200)
                        .message("User profile retrieved from Auth Server")
                        .data(profile)
                        .build()
        );
    }

    @PostMapping("/send-email")
    public ResponseEntity<ResponseStructure<CommunicationResponse>> sendEmailNotification(
            @Valid @RequestBody EmailRequest request) {
        CommunicationResponse response = userService.sendEmailNotification(request);
        return ResponseEntity.ok(
                ResponseStructure.<CommunicationResponse>builder()
                        .statusCode(200)
                        .message("Email dispatched via Auth Platform")
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/send-sms")
    public ResponseEntity<ResponseStructure<CommunicationResponse>> sendSmsNotification(
            @Valid @RequestBody SmsRequest request) {
        CommunicationResponse response = userService.sendSmsNotification(request);
        return ResponseEntity.ok(
                ResponseStructure.<CommunicationResponse>builder()
                        .statusCode(200)
                        .message("SMS dispatched via Auth Platform")
                        .data(response)
                        .build()
        );
    }
}
