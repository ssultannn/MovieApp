package com.example.MovieApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MovieApp.entity.WatchlistEntity;

public interface WatchlistRepository extends JpaRepository<WatchlistEntity, Long> {

    List<WatchlistEntity> findByUserId(Long userId);
}