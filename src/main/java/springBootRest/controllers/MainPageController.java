package springBootRest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springBootRest.models.Role;
import springBootRest.models.User;
import springBootRest.services.RoleService.RoleService;
import springBootRest.services.UserService.UserService;

import java.security.Principal;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/rest")
public class MainPageController {

    private final UserService userService;
    private final RoleService roleService;


    public MainPageController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String index(Principal principal, ModelMap model) {

        User user = userService.getUserByUsername(principal.getName());
        String rolesString = user.getRoles().stream().map(Role::getName).sorted().collect(Collectors.joining(", "));

        model.addAttribute("profile", user.getUsername());
        model.addAttribute("role_profile", rolesString);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());

        return "restTable";
    }
}
