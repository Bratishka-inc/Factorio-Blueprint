package ru.Factorio_Blueprint.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.Factorio_Blueprint.models.User;
import ru.Factorio_Blueprint.repositories.UserRepository;

@Component
public class SecurityUtil {

    private static UserRepository staticUserRepository;

    // Инжект репозитория через конструктор (для статического использования)
    public SecurityUtil(UserRepository userRepository) {
        SecurityUtil.staticUserRepository = userRepository;
    }

    public static User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        String username = auth.getName();
        return staticUserRepository.findByUsername(username).orElse(null);
    }

    public static String getCurrentUsernameOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();

        // Если principal — наш UserDetails то auth.getName() вернёт username
        if (principal instanceof org.springframework.security.core.userdetails.User u) {
            return u.getUsername();
        }

        // Если principal — строка (например anonymousUser)
        if (principal instanceof String s) {
            if (s.equals("anonymousUser")) return null;
            return s;
        }

        // fallback
        return auth.getName();
    }

}
