package springBootRest.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springBootRest.repositories.RoleRepository;
import springBootRest.repositories.UserRepository;
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

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public boolean saveUser(User user) {
        if(userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Transactional
    @Override
    public boolean removeUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if(!users.isEmpty()) {
            users.forEach(user1 -> user1.setRolesName(user1.getRoles()
                    .stream().map(Role::getName).sorted().collect(Collectors.joining(","))));
        }
        return users;
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(Long id) {
        Optional<User> user1 = userRepository.findById(id);
        User user = user1.orElse(null);
        if(user != null) {
            user.setRolesName(user.getRoles().stream().map(Role::getName).sorted().collect(Collectors.joining(",")));
        }
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public boolean updateUser(User user, String usernameOld, String passwordOld) {

        if (userRepository.findByUsername(user.getUsername()) != null) {
            if (!usernameOld.equals(user.getUsername())) {
                return false;
            }
        }
        if(!passwordOld.equals(user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Not Found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    public Set<Role> getRoles(String roleName) {
        Set<Role> roles = new HashSet<>();
        switch (roleName){
            case "ROLE_ADMIN":
                roles.add(roleRepository.findById(1L));
                break;
            case "ROLE_USER":
                roles.add(roleRepository.findById(2L));
                break;
            case "ROLE_ADMIN,ROLE_USER":
                roles.add(roleRepository.findById(1L));
                roles.add(roleRepository.findById(2L));
                break;
        }
        return roles;
    }
}