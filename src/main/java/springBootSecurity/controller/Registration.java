package springBootSecurity.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/formRegistration")
public class Registration {

    private final UserService userService;

    @Autowired
    public Registration(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String registration(ModelMap model) {
        model.addAttribute("user", new User());
        return "formRegistration";
    }

    @PostMapping("")
    public String registration(@RequestParam(value = "roleAdmin", required = false) String role,
                               @ModelAttribute("user") @Valid User userForm, BindingResult bindingR, ModelMap model) {
        if (bindingR.hasErrors()) {
            return "formRegistration";
        }
        Set <Role> roles = new HashSet<>();
        roles.add(new Role(2L, "ROLE_USER"));
        if (role != null) {
            roles.add(new Role(1L, "ROLE_ADMIN"));
        }
        userForm.setRoles(roles);
        if (!userService.saveUser(userForm)){
            model.addAttribute("userFormError", "Username already exists");
            return "formRegistration";
        }
        return "redirect:/";
    }
}
