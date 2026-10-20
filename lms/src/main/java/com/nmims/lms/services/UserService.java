package com.nmims.lms.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nmims.lms.context.UserContextHolder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sol.auth.client.principal.AuthUserPrincipal;
import sol.auth.client.sdk.UserManagementClient;
import sol.auth.common.dto.CommunicationResponse;
import sol.auth.common.dto.EmailRequest;
import sol.auth.common.dto.SmsRequest;
import sol.auth.common.dto.UserSummaryResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserManagementClient userManagementClient;

    public Optional<AuthUserPrincipal> getCurrentUser() {
        return UserContextHolder.getPrincipal();
    }

    public UserSummaryResponse getCurrentUserProfile() {
        Long userId = UserContextHolder.getUserId();
        if (userId == null) {
            log.warn("Cannot fetch profile: current user has no userId in token");
            return null;
        }
        return userManagementClient.getUserProfile(userId);
    }

    public UserSummaryResponse getUserProfileById(Long userId) {
        return userManagementClient.getUserProfile(userId);
    }

    public CommunicationResponse sendEmailNotification(EmailRequest request) {
        log.info("Sending email via Auth Platform UserManagementClient to {}", request.getTo());
        return userManagementClient.sendEmail(request);
    }

    public CommunicationResponse sendSmsNotification(SmsRequest request) {
        log.info("Sending SMS via Auth Platform UserManagementClient to {}", request.getPhoneNumber());
        return userManagementClient.sendSms(request);
    }
}