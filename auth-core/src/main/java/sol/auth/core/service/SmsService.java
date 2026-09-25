package sol.auth.core.service;

import sol.auth.common.dto.SmsRequest;

public interface SmsService {

    void sendSms(SmsRequest request);
}
