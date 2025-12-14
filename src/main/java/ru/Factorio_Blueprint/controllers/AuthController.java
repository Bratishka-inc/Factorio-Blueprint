package ru.Factorio_Blueprint.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.Factorio_Blueprint.dto.RegisterRequest;
import ru.Factorio_Blueprint.services.UserService;


@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/auth/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/auth/register")
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/auth/register")
    public String registerUser(@ModelAttribute("registerRequest") @Valid RegisterRequest registerRequest,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (userService.userExists(registerRequest.getUsername(), registerRequest.getEmail())) {
            result.rejectValue("username", null, "Username or email already exists");
        }
        if (result.hasErrors()) {
            // Оставаться на регистрационной странице
            return "auth/register";
        }
        userService.register(registerRequest);
        redirectAttributes.addFlashAttribute("registrationSuccess", true);
        return "redirect:/auth/login";
    }
}