package ru.Factorio_Blueprint.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.Factorio_Blueprint.models.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
