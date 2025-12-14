package ru.Factorio_Blueprint.services;

import ru.Factorio_Blueprint.dto.RegisterRequest;
import ru.Factorio_Blueprint.models.User;

import java.util.List;

public interface UserService {

    User register(RegisterRequest request);

    boolean userExists(String username, String email);

    User getCurrentUser();

    List<User> getAllUsers();

    void makeAdmin(Long userId);
}