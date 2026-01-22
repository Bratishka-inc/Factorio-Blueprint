package ru.Factorio_Blueprint.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.Factorio_Blueprint.models.Guide;
import ru.Factorio_Blueprint.services.GuideService;

@Controller
@RequestMapping("/guides")
public class GuideController {

    private final GuideService guideService;

    public GuideController(GuideService guideService) {
        this.guideService = guideService;
    }

    //лист всех чертежей
    @GetMapping
    public String listGuides(@RequestParam(value = "search", required = false) String search,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             Model model) {

        int size = 6; // Размер страницы подправить кода фронт будет
        Page<Guide> guidePage = guideService.getPaginatedGuides(search, page, size);

        model.addAttribute("guides", guidePage.getContent());
        model.addAttribute("totalPages", guidePage.getTotalPages());
        model.addAttribute("search", search);

        return "guides/list";
    }

    //Детали
    @GetMapping("/{id}")
    public String guideDetails(@PathVariable Long id, Model model) {

        Guide guide = guideService.getGuideById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Гайд не найден: " + id
                ));

        long likeCount = guideService.countLikes(id);
        boolean userLiked = guideService.hasUserLiked(id);

        model.addAttribute("guide", guide);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("userLiked", userLiked);

        return "guides/details";
    }


    /*
    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String showAddForm(Model model) {
        model.addAttribute("guide", new Guide());
        return "guides/add";
    }

    //Добавление
    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String addGuide(@ModelAttribute Guide guide) {

        guideService.saveGuide(guide);
        return "redirect:/guides?message=added";
    }*/

    //Изменение
    @GetMapping("/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String showEditForm(@PathVariable Long id, Model model) {

        Guide guide = guideService.getGuideById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Гайд не найден: " + id
                ));

        model.addAttribute("guide", guide);
        return "guides/edit";
    }

    //Изменение новое
    @PostMapping("/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String editGuide(@PathVariable Long id,
                            @ModelAttribute Guide guideUpdates) {

        try {
            guideUpdates.setId(id);
            guideService.updateGuide(guideUpdates);
        } catch (AccessDeniedException ex) {
            return "redirect:/error/403";
        }

        return "redirect:/guides/" + id + "?updated=true";
    }

    //Добавление
    @GetMapping("/new")
    @PreAuthorize("isAuthenticated()")
    public String showCreateForm(Model model) {
        model.addAttribute("guide", new Guide());
        return "guides/create";
    }

    //Добавление, новое
    @PostMapping("/new")
    @PreAuthorize("isAuthenticated()")
    public String createGuide(@ModelAttribute("guide") Guide guide) {
        guideService.saveGuide(guide);
        return "redirect:/guides";
    }

    //Удаление
    @PostMapping("/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteGuide(@PathVariable Long id) {

        try {
            guideService.deleteGuide(id);
        } catch (AccessDeniedException ex) {
            return "redirect:/error/403";
        }

        return "redirect:/guides?message=deleted";
    }

    @PostMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    public String likeGuide(@PathVariable Long id) {

        guideService.likeGuide(id);

        return "redirect:/guides/" + id;
    }

    @PostMapping("/{id}/unlike")
    @PreAuthorize("isAuthenticated()")
    public String unlikeGuide(@PathVariable Long id) {

        guideService.unlikeGuide(id);

        return "redirect:/guides/" + id;
    }



}
