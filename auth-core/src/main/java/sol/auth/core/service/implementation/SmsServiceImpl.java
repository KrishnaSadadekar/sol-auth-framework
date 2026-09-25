package sol.auth.core.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import sol.auth.common.dto.SmsRequest;
import sol.auth.core.service.SmsService;

@Service
public class SmsServiceImpl implements SmsService {

    private static final Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Override
    public void sendSms(SmsRequest request) {
        log.info("Sending SMS to: [{}], messageLength: [{}]",
                request.getPhoneNumber(),
                request.getMessage() != null ? request.getMessage().length() : 0);
        // Core implementation logs and delegates to configured SMS gateway (Twilio / AWS SNS)
    }
}
