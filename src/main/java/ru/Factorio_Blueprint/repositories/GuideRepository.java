package ru.Factorio_Blueprint.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.Factorio_Blueprint.models.Guide;

import java.util.List;

public interface GuideRepository extends JpaRepository<Guide, Long> {

    List<Guide> findByAuthorId(Long authorId);

    Page<Guide> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("""
    SELECT g, COUNT(gl.id)
    FROM Guide g
    LEFT JOIN GuideLike gl ON gl.guide = g
    GROUP BY g
    ORDER BY COUNT(gl.id) DESC
""")
    List<Object[]> findTopGuidesWithLikeCount(Pageable pageable);


}
