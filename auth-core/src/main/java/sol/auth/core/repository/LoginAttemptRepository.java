package sol.auth.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sol.auth.core.entity.LoginAttempt;

public interface LoginAttemptRepository  extends JpaRepository<LoginAttempt, Long> {

    
}
