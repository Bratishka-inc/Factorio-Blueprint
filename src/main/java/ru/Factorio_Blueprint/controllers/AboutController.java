package ru.Factorio_Blueprint.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// страничка "ОооооО"

@Controller
public class AboutController {

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }
}
