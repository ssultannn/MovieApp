package com.example.MovieApp.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.MovieApp.entity.MovieEntity;
import com.example.MovieApp.response.MovieResponse;

public interface MovieRepository extends JpaRepository<MovieEntity, Long>{
	List<MovieEntity> findByGenreContainingIgnoreCase(String genre);

	List<MovieEntity> findByImdbRatingGreaterThanEqual(double minRating);

    Optional<MovieEntity> findById(Long id); // Поиск фильма по ID


}
