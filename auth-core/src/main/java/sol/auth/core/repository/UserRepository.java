package sol.auth.core.repository;
import sol.auth.core.entity.User;   
import org.springframework.data.jpa.repository.JpaRepository;
    
public interface UserRepository extends  JpaRepository<User, Long> {
    
}
