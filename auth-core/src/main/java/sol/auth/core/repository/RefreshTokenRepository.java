package sol.auth.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sol.auth.core.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
}
