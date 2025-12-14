package ru.Factorio_Blueprint.services;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.Factorio_Blueprint.dto.AddBlueprintDto;
import ru.Factorio_Blueprint.dto.ShowBlueprintDto;
import ru.Factorio_Blueprint.models.Blueprint;
import ru.Factorio_Blueprint.models.Category;

import java.util.List;

public interface BlueprintService {

    List<ShowBlueprintDto> getAllBlueprintsDto();

    List<Blueprint> getAllBlueprints();

    Blueprint getBlueprintById(Long id);

    void createBlueprint(AddBlueprintDto dto);

    Page<Blueprint> getBlueprints(String search, Pageable pageable);

    void deleteBlueprint(Long id);

    void updateBlueprint(Long id, Blueprint updatedBlueprint);

    List<Category> getAllCategories();

}

