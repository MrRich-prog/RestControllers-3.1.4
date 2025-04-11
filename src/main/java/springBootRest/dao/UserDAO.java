package springBootRest.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import springBootRest.models.User;

public interface UserDAO extends JpaRepository<User, Long> {
    User findByUsername(String username);
}