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

    void cleanUsersTable();

    User getUserById(Long id);

    User getUserByUsername(String username);

    boolean updateUser(User user);

    Set<Role> getRoles(String roleName);
}