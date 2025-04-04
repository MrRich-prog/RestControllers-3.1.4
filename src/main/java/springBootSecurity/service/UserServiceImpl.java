package springBootSecurity.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springBootSecurity.dao.UserDAO;
import springBootSecurity.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public void removeUserById(Long id) {
        userDAO.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Transactional
    @Override
    public void cleanUsersTable() {
        userDAO.deleteAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(Long id) {
        Optional<User> user = userDAO.findById(id);
        return user.orElse(null);
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Transactional
    @Override
    public boolean updateUser(User user) {
        if(userDAO.findByUsername(user.getUsername()) != null) {
            return false;
        }
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

}