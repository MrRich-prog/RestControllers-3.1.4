package springBootRest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springBootRest.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}