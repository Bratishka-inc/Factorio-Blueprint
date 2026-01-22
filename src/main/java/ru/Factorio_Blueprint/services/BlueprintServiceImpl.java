package ru.Factorio_Blueprint.services;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import ru.Factorio_Blueprint.dto.AddBlueprintDto;
import ru.Factorio_Blueprint.dto.ShowBlueprintDto;
import ru.Factorio_Blueprint.exceptions.EntityNotFoundException;
import ru.Factorio_Blueprint.models.Blueprint;
import ru.Factorio_Blueprint.models.Category;
import ru.Factorio_Blueprint.models.User;
import ru.Factorio_Blueprint.repositories.BlueprintRepository;
import ru.Factorio_Blueprint.repositories.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BlueprintServiceImpl implements BlueprintService {

    private final BlueprintRepository blueprintRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public BlueprintServiceImpl(BlueprintRepository blueprintRepository,
                                CategoryRepository categoryRepository,
                                ModelMapper modelMapper,
                                UserService userService) {

        this.blueprintRepository = blueprintRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    //Прочти все
    @Override
    public List<Blueprint> getAllBlueprints() {
        return blueprintRepository.findAll();
    }

    //Прочти все но кэш
    @Override
    @Cacheable(cacheNames = "blueprints_all")
    public List<ShowBlueprintDto> getAllBlueprintsDto() {

        log.info("ЗАГРУЗКА ЧЕРТЕЖЕЙ ИЗ БД (без кэша)");

        return blueprintRepository.findAll()
                .stream()
                .map(bp -> modelMapper.map(bp, ShowBlueprintDto.class))
                .collect(Collectors.toList());
    }

    //Прочти это
    @Override
    public Blueprint getBlueprintById(Long id) {
        return blueprintRepository.findByIdWithAuthor(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Чертёж не найден: " + id));
    }

    //Поиск + пагинация
    @Override
    public Page<Blueprint> getBlueprints(String search, Pageable pageable) {
        if (search == null || search.isBlank()) {
            return blueprintRepository.findAll(pageable);
        }
        return blueprintRepository.findByNameContainingIgnoreCase(search, pageable);
    }

    //Создание
    @Override
    @CacheEvict(cacheNames = {"blueprints_all"}, allEntries = true)
    public void createBlueprint(AddBlueprintDto dto) {

        Blueprint bp = new Blueprint();
        bp.setName(dto.getName());
        bp.setDescription(dto.getDescription());
        bp.setBlueprintString(dto.getBlueprintString());
        bp.setImageUrl(dto.getImageUrl());

        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            bp.setAuthor(currentUser);
        }

        blueprintRepository.save(bp);

        log.info("Чертёж сохранён, кэш blueprints_all очищен");
    }

    //Обновление
    @Override
    @CacheEvict(cacheNames = {"blueprints_all"}, allEntries = true)
    public void updateBlueprint(Long id, Blueprint updated) {

        Blueprint bp = blueprintRepository.findByIdWithAuthor(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Чертёж не найден: " + id));

        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Необходимо войти в систему");
        }

        boolean isOwner = bp.getAuthor() != null &&
                bp.getAuthor().getId().equals(currentUser.getId());

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Редактирование запрещено");
        }

        bp.setName(updated.getName());
        bp.setDescription(updated.getDescription());
        bp.setBlueprintString(updated.getBlueprintString());
        bp.setImageUrl(updated.getImageUrl());

        blueprintRepository.save(bp);

        log.info("Чертёж обновлён, кэш blueprints_all очищен");
    }

    //Удаление
    @Override
    @CacheEvict(cacheNames = {"blueprints_all"}, allEntries = true)
    public void deleteBlueprint(Long id) {

        Blueprint bp = blueprintRepository.findByIdWithAuthor(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Чертёж не найден: " + id));

        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Необходимо войти в систему");
        }

        boolean isOwner = bp.getAuthor() != null &&
                bp.getAuthor().getId().equals(currentUser.getId());

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Удаление запрещено");
        }

        blueprintRepository.delete(bp);

        log.info("Чертёж удалён, кэш blueprints_all очищен");
    }

    //Категории кэшируются
    @Override
    @Cacheable(cacheNames = "blueprint_categories")
    public List<Category> getAllCategories() {

        log.info("ЗАГРУЗКА КАТЕГОРИЙ ИЗ БД (должно быть 1 раз)"); //проверка на дурака

        return categoryRepository.findAll();
    }
}
