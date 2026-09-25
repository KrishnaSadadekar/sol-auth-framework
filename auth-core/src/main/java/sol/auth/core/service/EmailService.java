package sol.auth.core.service;

import sol.auth.common.dto.EmailRequest;

public interface EmailService {

    void sendEmail(EmailRequest request);

    void sendSimpleEmail(String to, String subject, String body);
}
