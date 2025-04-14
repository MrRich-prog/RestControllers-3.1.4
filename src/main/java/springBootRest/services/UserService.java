package springBootRest.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import springBootRest.models.Role;
import springBootRest.models.User;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {

    boolean saveUser(User user);

    boolean removeUserById(Long id);

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByUsername(String username);

    boolean updateUser(User user, String username, String password);

    Set<Role> getRoles(String roleName);
}