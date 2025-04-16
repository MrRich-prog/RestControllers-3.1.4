package springBootRest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springBootRest.models.User;
import springBootRest.services.RoleService.RoleService;
import springBootRest.services.UserService.UserService;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest")
public class RestController {

    private final UserService userService;
    private final RoleService roleService;

    public RestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody User user, @RequestParam(value = "roles") List<String> roles) {
        System.out.println(roles);
        System.out.println(roleService.getRoleByName(roles));

        user.setRoles(roleService.getRoleByName(roles));
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getUsers() {
        final List<User> users = userService.getAllUsers();
        return users != null && !users.isEmpty() ? new ResponseEntity<>(users, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser(@RequestParam Long id) {
        User user = userService.getUserById(id);

        return user != null ? new ResponseEntity<>(user, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody User user,
                                    @RequestParam(value = "usernameOld") String usernameOld,
                                    @RequestParam(value = "passwordOld") String passwordOld,
                                    @RequestParam(value = "roles") List<String> roles) {

        System.out.println(roleService.getRoleByName(roles));
        user.setRoles(roleService.getRoleByName(roles));

        boolean updated = userService.updateUser(user, usernameOld, passwordOld);
        if (!updated) {
            System.out.println("Username already exists");
        }
        return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> remove(@RequestParam Long id) {
        boolean deleted = userService.removeUserById(id);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
