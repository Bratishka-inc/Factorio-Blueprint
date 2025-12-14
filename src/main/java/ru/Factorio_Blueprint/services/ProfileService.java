package ru.Factorio_Blueprint.services;

import ru.Factorio_Blueprint.models.Blueprint;
import ru.Factorio_Blueprint.models.Guide;
import ru.Factorio_Blueprint.models.User;

import java.util.List;

public interface ProfileService {
    User getCurrentUser();
    List<Blueprint> getUserBlueprints(Long userId);
    List<Guide> getUserGuides(Long userId);
}
