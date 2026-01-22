package ru.Factorio_Blueprint.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ru.Factorio_Blueprint.dto.AddBlueprintDto;
import ru.Factorio_Blueprint.models.Blueprint;
import ru.Factorio_Blueprint.security.SecurityUtil;
import ru.Factorio_Blueprint.services.BlueprintService;

@Controller
@RequestMapping("/blueprints")
public class BlueprintController {

    private final BlueprintService blueprintService;

    public BlueprintController(BlueprintService blueprintService) {
        this.blueprintService = blueprintService;
    }


    //лист всех чертежей
    @GetMapping
    public String listBlueprints(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            Model model
    ) {

        Page<Blueprint> bpPage =
                blueprintService.getBlueprints(search, PageRequest.of(page, size));

        // СПИСОК
        model.addAttribute("blueprints", bpPage.getContent());

        // ПАГИНАЦИЯ
        model.addAttribute("totalPages", bpPage.getTotalPages());
        model.addAttribute("currentPage", page);

        // ПОИСК
        model.addAttribute("search", search);

        // КАТЕГОРИИ
        model.addAttribute("categories", blueprintService.getAllCategories());

        return "blueprints/list";
    }



    // ДЕТАЛИЗАЦИЯ чертежей, доступна всем
    @GetMapping("/{id}")
    public String blueprintDetails(@PathVariable Long id, Model model) {
        Blueprint bp = blueprintService.getBlueprintById(id);

        // определение владельца
        boolean isOwner = false;
        String current = SecurityUtil.getCurrentUsernameOrNull();

        if (current != null && bp.getAuthor() != null &&
                current.equals(bp.getAuthor().getUsername())) {
            isOwner = true;
        }

        model.addAttribute("blueprint", bp);
        model.addAttribute("isOwner", isOwner);

        return "blueprints/details";
    }


    //Добавление ТОЛЬКО ДЛЯ ЗАЛОГИНЕННЫХ!!!
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("addBlueprintDto", new AddBlueprintDto());
        return "blueprints/add";
    }


    //Добавление (Новый метод) ТОЛЬКО ДЛЯ ЗАЛОГИНЕННЫХ!!!
    @PostMapping("/add")
    public String addBlueprint(@Valid @ModelAttribute AddBlueprintDto addBlueprintDto,
                               BindingResult bindingResult,
                               Model model) {

        if (bindingResult.hasErrors()) {
            return "blueprints/add";
        }

        blueprintService.createBlueprint(addBlueprintDto);
        return "redirect:/blueprints?message=added";
    }


    //Изменение чертежа, только ВЛАДЕЛЕЦ/АДМИН
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {

        Blueprint bp = blueprintService.getBlueprintById(id);

        String current = SecurityUtil.getCurrentUsernameOrNull();
        boolean isOwner = current != null && bp.getAuthor() != null &&
                current.equals(bp.getAuthor().getUsername());

        if (!isOwner) {
            throw new AccessDeniedException("Не владелец");
        }

        model.addAttribute("blueprint", bp);
        return "blueprints/edit";
    }


    //(Новый метод) Изменение чертежа, только ВЛАДЕЛЕЦ/АДМИН
    @PostMapping("/{id}/edit")
    public String editBlueprint(@PathVariable Long id,
                                @ModelAttribute Blueprint blueprintUpdates) {

        try {
            blueprintService.updateBlueprint(id, blueprintUpdates);
        }
        catch (AccessDeniedException ex) {
            return "redirect:/error/403";
        }

        return "redirect:/blueprints/" + id + "?updated=true";
    }


    //Удаление, ТОЛКЬО ДЛЯ ЗАЛОГИНЕННЫХ!!!
    @PostMapping("/{id}/delete")
    public String deleteBlueprint(@PathVariable Long id) {

        try {
            blueprintService.deleteBlueprint(id);
        }
        catch (AccessDeniedException ex) {
            return "redirect:/error/403";
        }

        return "redirect:/blueprints?message=deleted";
    }

}
