package sol.auth.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sol.auth.core.entity.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    
}
