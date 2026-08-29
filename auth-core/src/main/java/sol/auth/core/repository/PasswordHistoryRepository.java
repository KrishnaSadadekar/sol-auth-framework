package sol.auth.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sol.auth.core.entity.PasswordHistory;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {

    List<PasswordHistory> findByUserIdOrderByCreatedAtDesc(Long id);

}
