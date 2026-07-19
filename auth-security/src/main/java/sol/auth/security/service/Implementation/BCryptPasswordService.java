package sol.auth.security.service.Implementation;

import org.springframework.security.crypto.password.PasswordEncoder;

import sol.auth.core.service.PasswordService;

public class BCryptPasswordService implements PasswordService {

    private final PasswordEncoder passwordEncoder;

    public BCryptPasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String rawPassword) {

        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        // TODO Auto-generated method stub
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
