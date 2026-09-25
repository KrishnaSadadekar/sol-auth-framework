package sol.auth.service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sol.auth.common.dto.CommunicationResponse;
import sol.auth.common.dto.EmailRequest;
import sol.auth.common.dto.SmsRequest;
import sol.auth.core.service.EmailService;
import sol.auth.core.service.SmsService;

@RestController
@RequestMapping("/api/v1/communications")
@Validated
public class CommunicationController {

    private final EmailService emailService;
    private final SmsService smsService;

    public CommunicationController(EmailService emailService, SmsService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

    @PostMapping("/email")
    public ResponseEntity<CommunicationResponse> sendEmail(@RequestBody @Valid EmailRequest request) {
        emailService.sendEmail(request);
        String messageId = UUID.randomUUID().toString();
        return ResponseEntity.ok(CommunicationResponse.ok(messageId, "Email queued for delivery successfully"));
    }

    @PostMapping("/sms")
    public ResponseEntity<CommunicationResponse> sendSms(@RequestBody @Valid SmsRequest request) {
        smsService.sendSms(request);
        String messageId = UUID.randomUUID().toString();
        return ResponseEntity.ok(CommunicationResponse.ok(messageId, "SMS queued for delivery successfully"));
    }
}
