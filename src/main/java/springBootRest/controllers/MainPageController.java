package springBootRest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springBootRest.models.Role;
import springBootRest.models.User;
import springBootRest.services.UserService;

import java.security.Principal;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/rest")
public class MainPageController {

    private final UserService userService;

    public MainPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String index(Principal principal, ModelMap model) {

        User user = userService.getUserByUsername(principal.getName());
        String rolesString = user.getRoles().stream().map(Role::getName).sorted().collect(Collectors.joining(", "));

        model.addAttribute("profile", user.getUsername());
        model.addAttribute("role_profile", rolesString);
        model.addAttribute("user", user);

        return "restTable";
    }
}
