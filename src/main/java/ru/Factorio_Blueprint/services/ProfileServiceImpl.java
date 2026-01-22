package ru.Factorio_Blueprint.services;

import org.springframework.stereotype.Service;
import ru.Factorio_Blueprint.models.Blueprint;
import ru.Factorio_Blueprint.models.Guide;
import ru.Factorio_Blueprint.models.User;
import ru.Factorio_Blueprint.repositories.BlueprintRepository;
import ru.Factorio_Blueprint.repositories.GuideRepository;
import ru.Factorio_Blueprint.security.SecurityUtil;

import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final BlueprintRepository blueprintRepository;
    private final GuideRepository guideRepository;

    public ProfileServiceImpl(BlueprintRepository blueprintRepository,
                              GuideRepository guideRepository) {
        this.blueprintRepository = blueprintRepository;
        this.guideRepository = guideRepository;
    }

    @Override
    public User getCurrentUser() {
        return SecurityUtil.getCurrentUser();
    }

    @Override
    public List<Blueprint> getUserBlueprints(Long userId) {
        return blueprintRepository.findByAuthorId(userId);
    }

    @Override
    public List<Guide> getUserGuides(Long userId) {
        return guideRepository.findByAuthorId(userId);
    }
}
