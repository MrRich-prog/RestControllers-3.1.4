package springBootSecurity.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import springBootSecurity.models.User;

public interface UserDAO extends JpaRepository<User, Long> {

    User findByUsername(String username);

}