package ru.Factorio_Blueprint.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.Factorio_Blueprint.models.User;
import ru.Factorio_Blueprint.services.UserService;

import java.util.List;


@Controller
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/panel")
    public String adminPanel() {
        return "admin/panel";
    }

    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/admin/makeAdmin/{id}")
    public String makeAdmin(@PathVariable("id") Long id) {
        userService.makeAdmin(id);
        return "redirect:/admin/users";
    }
}