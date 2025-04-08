package springBootSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springBootSecurity.models.Role;
import springBootSecurity.models.User;
import springBootSecurity.service.UserService;

import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserPage {

    private final UserService userService;

    @Autowired
    public UserPage(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "")
    public String userPage(Principal principal, ModelMap model) {

        User user = userService.getUserByUsername(principal.getName());
        String rolesString = user.getRoles().stream().map(Role::getName).sorted().collect(Collectors.joining(", "));

        model.addAttribute("role", rolesString);
        model.addAttribute("user", user);
        return "user";
    }
}
