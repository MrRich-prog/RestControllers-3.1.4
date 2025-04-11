package springBootRest.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springBootRest.dao.UserDAO;
import springBootRest.models.Role;
import springBootRest.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDAO = userDAO;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    @Override
    public boolean saveUser(User user) {
        if(userDAO.findByUsername(user.getUsername()) != null) {
            return false;
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDAO.save(user);
        return true;
    }

    @Transactional
    @Override
    public boolean removeUserById(Long id) {
        Optional<User> user = userDAO.findById(id);
        if(user.isEmpty()) {
            return false;
        }
        userDAO.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        List<User> users = userDAO.findAll();
        if(!users.isEmpty()) {
            users.forEach(user1 -> user1.setRolesName(user1.getRoles()
                    .stream().map(Role::getName).sorted().collect(Collectors.joining(","))));
        }
        return users;
    }

    @Transactional
    @Override
    public void cleanUsersTable() {
        userDAO.deleteAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(Long id) {
        Optional<User> user1 = userDAO.findById(id);
        User user = user1.orElse(null);
        if(user != null) {
            user.setRolesName(user.getRoles().stream().map(Role::getName).sorted().collect(Collectors.joining(", ")));
        }
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Transactional
    @Override
    public boolean updateUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDAO.save(user);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Not Found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    public Set<Role> getRoles(String roleName) {
        Set<Role> roles = new HashSet<>();
        switch (roleName){
            case "ROLE_ADMIN":
                roles.add(new Role(1L, "ROLE_ADMIN"));
                break;
            case "ROLE_USER":
                roles.add(new Role(2L, "ROLE_USER"));
                break;
            case "ROLE_ADMIN, ROLE_USER":
                roles.add(new Role(1L, "ROLE_ADMIN"));
                roles.add(new Role(2L, "ROLE_USER"));
                break;
        }
        return roles;
    }
}