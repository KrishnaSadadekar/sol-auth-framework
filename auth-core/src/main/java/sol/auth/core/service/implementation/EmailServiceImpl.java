package sol.auth.core.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import sol.auth.common.dto.EmailRequest;
import sol.auth.core.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void sendEmail(EmailRequest request) {
        log.info("Sending email to: [{}], subject: [{}], template: [{}]",
                request.getTo(), request.getSubject(), request.getTemplateName());
        // Core implementation handles templated or simple dispatch
        // Can be backed by JavaMailSender / SendGrid / AWS SES in production
    }

    @Override
    public void sendSimpleEmail(String to, String subject, String body) {
        EmailRequest request = EmailRequest.builder()
                .to(to)
                .subject(subject)
                .body(body)
                .html(false)
                .build();
        sendEmail(request);
    }
}
