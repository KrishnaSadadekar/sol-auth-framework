package sol.auth.core.service;


import sol.auth.core.dto.LoginRequest;
import sol.auth.core.entity.User;

public interface AuthenticationService {

    User login(LoginRequest request);
}
