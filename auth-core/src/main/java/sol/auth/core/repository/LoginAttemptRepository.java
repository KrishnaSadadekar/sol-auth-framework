package sol.auth.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sol.auth.core.entity.LoginAttempt;
import sol.auth.core.enums.LoginStatus;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
    long countByUsernameAndStatusNot(String username, LoginStatus status);

    List<LoginAttempt> findTop5ByUsernameOrderByCreatedAtDesc(String username);

    long countByUsernameAndStatus(String username, LoginStatus status);
}
