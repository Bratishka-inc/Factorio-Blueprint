package ru.Factorio_Blueprint.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.Factorio_Blueprint.models.User;
import ru.Factorio_Blueprint.repositories.UserRepository;
import ru.Factorio_Blueprint.services.ProfileService;

@Controller
public class ProfileController {

    private final ProfileService profileService;
    private final UserRepository userRepository;

    public ProfileController(ProfileService profileService,
                             UserRepository userRepository) {
        this.profileService = profileService;
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String profile(
            @RequestParam(value = "user", required = false) Long userId,
            Model model
    ) {
        User current = profileService.getCurrentUser();

        if (current == null) {
            return "redirect:/auth/login"; //Проверка на дурака
        }

        User targetUser;

        if (userId == null) {
            targetUser = current;
        } else {
            targetUser = userRepository.findById(userId).orElse(null);
            if (targetUser == null) {
                model.addAttribute("error", "Пользователь не найден");
                return "profile/profile";
            }
        }

        model.addAttribute("user", targetUser);
        model.addAttribute("blueprints", profileService.getUserBlueprints(targetUser.getId()));
        model.addAttribute("guides", profileService.getUserGuides(targetUser.getId()));

        return "profile/profile";
    }
}
