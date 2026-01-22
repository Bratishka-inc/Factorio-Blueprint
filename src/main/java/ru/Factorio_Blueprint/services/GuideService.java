package ru.Factorio_Blueprint.services;

import org.springframework.data.domain.Page;
import ru.Factorio_Blueprint.dto.GuideWithLikes;
import ru.Factorio_Blueprint.models.Guide;

import java.util.List;
import java.util.Optional;

public interface GuideService {

    // Все гайды (может использоваться админ-панелью / профилем)
    List<Guide> getAllGuides();

    // Гайды конкретного пользователя (для профиля)
    List<Guide> getGuidesByUserId(Long userId);

    // Один гайд по id — Optional, чтобы контроллер сам решал, что делать, если нет
    Optional<Guide> getGuideById(Long id);

    // Создание/сохранение гайда от имени текущего пользователя
    void saveGuide(Guide guide);

    // Обновление существующего гайда (поля title/category/description)
    void updateGuide(Guide guide);

    // Удаление гайда
    void deleteGuide(Long id);

    // Пагинация + поиск (под список гайдов)
    Page<Guide> getPaginatedGuides(String search, int page, int size);
    /*
    // На всякий случай для старых методов
    default Guide createGuide(Guide guide) {
        saveGuide(guide);
        return guide;
    }*/

    List<GuideWithLikes> getTopGuidesWithLikes(int limit);

    void likeGuide(Long guideId);
    void unlikeGuide(Long guideId);
    long countLikes(Long guideId);
    boolean hasUserLiked(Long guideId);

}
