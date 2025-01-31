package com.example.MovieApp.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.MovieApp.entity.UserEntity;
import com.example.MovieApp.repository.UserRepository;

@Component
public class UserContext {

    private final UserRepository userRepository;

    public UserContext(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Возвращает ID текущего аутентифицированного пользователя.
     * Используется имя пользователя для нахождения ID.
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Проверка на анонимного пользователя
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Невозможно определить ID пользователя: пользователь не аутентифицирован");
        }

        String username = authentication.getName(); // Получаем имя пользователя

        // Находим пользователя по имени и возвращаем его ID
        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalStateException("Пользователь с именем " + username + " не найден в базе данных");
        }

        return user.getId(); // Получаем ID пользователя
    }
}
