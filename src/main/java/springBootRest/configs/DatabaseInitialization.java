package springBootRest.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import springBootRest.models.Role;
import springBootRest.models.User;
import springBootRest.repositories.RoleRepository;
import springBootRest.services.UserService;

import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseInitialization implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserService userService;

    public DatabaseInitialization(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        roleRepository.save(new Role(2L, "ROLE_USER"));
        roleRepository.save(new Role(1L, "ROLE_ADMIN"));

        Set<Role> roles = new HashSet<>();
        roles.add(new Role(2L, "ROLE_USER"));
        Set<Role> roles2 = new HashSet<>();
        roles2.add(new Role(1L, "ROLE_ADMIN"));
        roles2.add(new Role(2L, "ROLE_USER"));

        userService.saveUser(new User("User","1234",14,roles));
        userService.saveUser(new User("Admin","1234",14,roles2));

    }
}
