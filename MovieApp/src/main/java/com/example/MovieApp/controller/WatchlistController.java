package com.example.MovieApp.controller;

import com.example.MovieApp.context.UserContext;
import com.example.MovieApp.entity.MovieEntity;
import com.example.MovieApp.entity.WatchlistEntity;
import com.example.MovieApp.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/watchlist")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;

    @Autowired
    private UserContext userContext;

    // Получить список фильмов для текущего пользователя (извлекается из контекста безопасности)
    @GetMapping("/current")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<MovieEntity> getUserWatchlist() {
        Long userId = userContext.getCurrentUserId();  // Получение userId из контекста безопасности
        return watchlistService.getUserWatchlist(userId);
    }

    // Добавить фильм в список для текущего пользователя
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String addToWatchlist(@RequestParam Long movieId) {
        Long userId = userContext.getCurrentUserId();  // Получение userId из контекста безопасности
        return watchlistService.addToWatchlist(userId, movieId);
    }

    // Удалить фильм из списка для текущего пользователя
    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String removeFromWatchlist(@RequestParam Long movieId) {
        Long userId = userContext.getCurrentUserId();  // Получение userId из контекста безопасности
        return watchlistService.removeFromWatchlist(userId, movieId);
    }
}
