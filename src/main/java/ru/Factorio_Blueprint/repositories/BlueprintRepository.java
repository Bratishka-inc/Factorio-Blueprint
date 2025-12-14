package ru.Factorio_Blueprint.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.Factorio_Blueprint.models.Blueprint;

import java.util.List;
import java.util.Optional;

public interface BlueprintRepository extends JpaRepository<Blueprint, Long> {

    // Поиск по названию для списка
    Page<Blueprint> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Для профиля пользователя (его чертежи)
    List<Blueprint> findByAuthorId(Long authorId);

    // Загрузка чертежа с автором и категорией
    @Query("""
           SELECT b FROM Blueprint b
           LEFT JOIN FETCH b.author
           LEFT JOIN FETCH b.category
           WHERE b.id = :id
           """)
    Optional<Blueprint> findByIdWithAuthor(@Param("id") Long id);
}
