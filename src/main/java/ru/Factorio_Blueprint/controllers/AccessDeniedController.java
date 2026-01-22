package ru.Factorio_Blueprint.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// ошибка 403

@Controller
public class AccessDeniedController {

    @GetMapping("/error/403")
    public String accessDenied() {
        return "error/403";
    }
}
