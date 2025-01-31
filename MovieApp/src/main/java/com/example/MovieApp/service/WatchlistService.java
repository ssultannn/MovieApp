package com.example.MovieApp.service;

import com.example.MovieApp.entity.MovieEntity;
import com.example.MovieApp.entity.WatchlistEntity;
import com.example.MovieApp.repository.MovieRepository;
import com.example.MovieApp.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
@Service
public class WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private MovieRepository movieRepository;

    // Получение списка фильмов в избранном пользователя
    public List<MovieEntity> getUserWatchlist(Long userId) {
        List<WatchlistEntity> watchlist = watchlistRepository.findByUserId(userId);
        return watchlist.stream()
                .map(watchlistItem -> movieRepository.findById(watchlistItem.getMovieId()).orElse(null))
                .filter(Objects::nonNull) // Убираем null, если фильм не найден
                .toList();
    }

    // Добавление фильма в избранное
    public String addToWatchlist(Long userId, Long movieId) {
        WatchlistEntity watchlistItem = new WatchlistEntity();
        watchlistItem.setUserId(userId);
        watchlistItem.setMovieId(movieId);
        watchlistRepository.save(watchlistItem);
        return "Movie added to watchlist!";
    }

    // Удаление фильма из избранного
    public String removeFromWatchlist(Long userId, Long movieId) {
        WatchlistEntity watchlistItem = watchlistRepository
                .findByUserId(userId)
                .stream()
                .filter(w -> w.getMovieId().equals(movieId))
                .findFirst()
                .orElse(null);

        if (watchlistItem != null) {
            watchlistRepository.delete(watchlistItem);
            return "Movie removed from watchlist!";
        }
        return "Movie not found in watchlist!";
    }
}

