package springBootRest.services.UserService;

import org.springframework.security.core.userdetails.UserDetailsService;
import springBootRest.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    boolean saveUser(User user);

    boolean removeUserById(Long id);

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByUsername(String username);

    boolean updateUser(User user, String username, String password);

}