package ru.Factorio_Blueprint.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.Factorio_Blueprint.services.GuideService;

@Controller
public class HomeController {

    private final GuideService guideService;

    public HomeController(GuideService guideService) {
        this.guideService = guideService;
    }

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute(
                "topGuides",
                guideService.getTopGuidesWithLikes(10)
        );


        return "index";
    }
}
