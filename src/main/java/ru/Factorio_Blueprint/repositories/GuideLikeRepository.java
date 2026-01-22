package ru.Factorio_Blueprint.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Factorio_Blueprint.models.GuideLike;

@Repository
public interface GuideLikeRepository extends JpaRepository<GuideLike, Long> {

    boolean existsByGuide_IdAndUser_Id(Long guideId, Long userId);

    long countByGuide_Id(Long guideId);

    void deleteByGuide_IdAndUser_Id(Long guideId, Long userId);
}
