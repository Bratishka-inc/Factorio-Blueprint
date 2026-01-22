package ru.Factorio_Blueprint.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.Factorio_Blueprint.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);
}
