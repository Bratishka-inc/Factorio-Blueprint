package ru.Factorio_Blueprint.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.Factorio_Blueprint.dto.GuideWithLikes;
import ru.Factorio_Blueprint.exceptions.EntityNotFoundException;
import ru.Factorio_Blueprint.models.Guide;
import ru.Factorio_Blueprint.models.GuideLike;
import ru.Factorio_Blueprint.models.User;
import ru.Factorio_Blueprint.repositories.GuideLikeRepository;
import ru.Factorio_Blueprint.repositories.GuideRepository;
import ru.Factorio_Blueprint.security.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GuideServiceImpl implements GuideService {

    private final GuideRepository guideRepository;
    private final GuideLikeRepository likeRepository;

    public GuideServiceImpl(GuideRepository guideRepository,
                            GuideLikeRepository likeRepository) {
        this.guideRepository = guideRepository;
        this.likeRepository = likeRepository;
    }

    //Прочти все
    @Override
    public List<Guide> getAllGuides() {
        return guideRepository.findAll();
    }

    //прочти Юзера
    @Override
    public List<Guide> getGuidesByUserId(Long userId) {
        return guideRepository.findByAuthorId(userId);
    }

    //Прочти ID
    @Override
    public Optional<Guide> getGuideById(Long id) {
        return guideRepository.findById(id);
    }

    //Создать/сохранить
    @Override
    public void saveGuide(Guide guide) {

        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Необходимо войти в систему");
        }

        if (guide.getId() == null) {
            guide.setAuthor(currentUser);
        } else {
            Guide existing = guideRepository.findById(guide.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Гайд не найден: " + guide.getId()));

            boolean isOwner = existing.getAuthor() != null
                    && existing.getAuthor().getId().equals(currentUser.getId());

            boolean isAdmin = currentUser.getRoles().stream()
                    .anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));

            if (!isOwner && !isAdmin) {
                throw new AccessDeniedException("У вас нет прав для изменения этого гайда");
            }

            existing.setTitle(guide.getTitle());
            existing.setCategory(guide.getCategory());
            existing.setDescription(guide.getDescription());
            guideRepository.save(existing);
            return;
        }

        guideRepository.save(guide);
    }


    //Обновление
    @Override
    public void updateGuide(Guide updated) {

        if (updated.getId() == null) {
            throw new IllegalArgumentException("ID гайда не может быть null при обновлении");
        }

        Guide guide = guideRepository.findById(updated.getId())
                .orElseThrow(() -> new EntityNotFoundException("Гайд не найден: " + updated.getId()));

        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Необходимо войти в систему");
        }

        boolean isOwner = guide.getAuthor() != null &&
                guide.getAuthor().getId().equals(currentUser.getId());

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("У вас нет прав на редактирование гайда");
        }

        guide.setTitle(updated.getTitle());
        guide.setCategory(updated.getCategory());
        guide.setDescription(updated.getDescription());

        guideRepository.save(guide);
    }


    //Удаление
    @Override
    public void deleteGuide(Long id) {

        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Гайд не найден: " + id));

        User currentUser = SecurityUtil.getCurrentUser();

        if (currentUser == null) {
            throw new AccessDeniedException("Необходимо войти в систему");
        }

        boolean isOwner = guide.getAuthor() != null
                && guide.getAuthor().getId().equals(currentUser.getId());

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("У вас нет прав для удаления этого гайда");
        }

        guideRepository.delete(guide);
    }


    //Пагинация и поиск
    @Override
    public Page<Guide> getPaginatedGuides(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        if (search != null && !search.isBlank()) {
            return guideRepository.findByTitleContainingIgnoreCase(search, pageable);
        } else {
            return guideRepository.findAll(pageable);
        }
    }

    /*
    @Override
    public Guide createGuide(Guide guide) {
        saveGuide(guide);
        return guide;
    }*/


    //Лайк
    @Override
    public void likeGuide(Long guideId) {

        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Необходимо войти в систему");
        }

        if (likeRepository.existsByGuide_IdAndUser_Id(guideId, currentUser.getId())) {
            return; // уже лайкнул
        }

        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new EntityNotFoundException("Гайд не найден: " + guideId));

        GuideLike like = new GuideLike(guide, currentUser);
        likeRepository.save(like);
    }

    //Удалил лайк
    @Transactional
    @Override
    public void unlikeGuide(Long guideId) {

        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Необходимо войти в систему");
        }

        likeRepository.deleteByGuide_IdAndUser_Id(guideId, currentUser.getId());
    }

    //Считать лайк
    @Override
    public long countLikes(Long guideId) {
        return likeRepository.countByGuide_Id(guideId);
    }

    //Был лайк?
    @Override
    public boolean hasUserLiked(Long guideId) {

        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) return false;

        return likeRepository.existsByGuide_IdAndUser_Id(guideId, currentUser.getId());
    }

    //Топ 10 по лайкам
    @Override
    public List<GuideWithLikes> getTopGuidesWithLikes(int limit) {

        return guideRepository
                .findTopGuidesWithLikeCount(PageRequest.of(0, limit))
                .stream()
                .map(row -> {
                    Guide g = (Guide) row[0];
                    long likes = (long) row[1];

                    return new GuideWithLikes(
                            g.getId(),
                            g.getTitle(),
                            g.getCategory(),
                            likes
                    );
                })
                .toList();
    }

}
