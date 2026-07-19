package sol.auth.core.service;

import sol.auth.core.dto.RegisterRequest;
import sol.auth.core.entity.User;

public interface RegistrationService {
     User register(RegisterRequest request);
}
