package springBootSecurity.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import springBootSecurity.models.Role;
import springBootSecurity.service.UserService;
import springBootSecurity.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminPage {

    private final UserService userService;

    @Autowired
    public AdminPage(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "")
    public String adminPage(Principal principal, ModelMap model) {
        User user = userService.getUserByUsername(principal.getName());
        String rolesString = user.getRoles().stream().map(Role::getName).collect(Collectors.joining(", "));
        model.addAttribute("role", rolesString);
        model.addAttribute("user", user);
        return "admin";
    }

    @PostMapping(value = "/saveUser")
    public String saveUser(@RequestParam(value = "roleAdmin", required = false) String role,
                           @ModelAttribute @Valid User user, ModelMap model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "AddUser"; // Вернуть обратно на форму с ошибками
        }
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(2L, "ROLE_USER"));
        if (role != null) {
            roles.add(new Role(1L, "ROLE_ADMIN"));
        }
        user.setRoles(roles);
        if (!userService.saveUser(user)){
            model.addAttribute("userFormError", "Username already exists");
            return "AddUser";
        }
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "Table";
    }

    @GetMapping(value = "/AddUserPage")
    public String index(ModelMap model) {
        model.addAttribute("user", new User());
        return "AddUser";
    }

    @GetMapping(value = "/UpdateUserPage")
    public String update(ModelMap model) {
        model.addAttribute("userFind", false);
        model.addAttribute("user", new User());
        return "UpdateUser";
    }

    @PostMapping(value = "/removeUserById")
    public String removeUserById(@RequestParam Long id, ModelMap model) {
        userService.removeUserById(id);
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "Table";
    }

    @GetMapping(value = "/getAllUsers")
    public String getAllUsers(ModelMap model) {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            user.setRolesName(user.getRoles().stream().map(Role::getName).collect(Collectors.joining(",")));
        }
        model.addAttribute("users", users);
        return "Table";
    }

    @PostMapping(value = "/cleanUserTable")
    public String cleanUserTable(ModelMap model) {
        userService.cleanUsersTable();
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "Table";
    }

    @GetMapping(value = "/getUserById")
    public String getUserById(@RequestParam Long id, ModelMap model) {
        User user = userService.getUserById(id);
        model.addAttribute("userFind", user != null);
        model.addAttribute("user", user);
        return "UpdateUser";
    }

    @PostMapping(value = "/updateUser")
    public String updateUser(@RequestParam(value = "roleAdmin", required = false) String role,
                             @ModelAttribute @Valid User user, ModelMap model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "UpdateUser";
        }
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(2L, "ROLE_USER"));
        if (role != null) {
            roles.add(new Role(1L, "ROLE_ADMIN"));
        } else {
            roles.remove(new Role(1L, "ROLE_ADMIN"));
        }
        user.setRoles(roles);
        if (!userService.updateUser(user)){
            model.addAttribute("userFormError", "Username already exists");
            return "UpdateUser";
        }
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "Table";
    }

}
