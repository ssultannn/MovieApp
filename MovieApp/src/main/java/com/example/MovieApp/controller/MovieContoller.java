package com.example.MovieApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.MovieApp.context.UserContext;
import com.example.MovieApp.request.MovieRequest;
import com.example.MovieApp.response.MovieResponse;
import com.example.MovieApp.service.MovieService;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieContoller {
	@Autowired
	private MovieService movieService;

	
	  @Autowired
	    private UserContext userContext;
	  
	  @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MovieResponse> createMovie(@RequestBody MovieRequest movieRequest) {
		  Long userId = userContext.getCurrentUserId();
		MovieResponse movieResponse = movieService.createMovie(movieRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(movieResponse);
	}

	@GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")

	public ResponseEntity<List<MovieResponse>> getAllMovies() {
		Long userId = userContext.getCurrentUserId();
		List<MovieResponse> movies = movieService.getAllMovies();
		return ResponseEntity.ok(movies);
	}

	@GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")

	public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long id) {
		
		MovieResponse movieResponse = movieService.getMovieById(id);
		return ResponseEntity.ok(movieResponse);
	}

	@PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")

	public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id, @RequestBody MovieRequest movieRequest) {
		
		Long userId = userContext.getCurrentUserId();
		MovieResponse updatedMovie = movieService.updateMovie(id, movieRequest);
		return ResponseEntity.ok(updatedMovie);
	}

	@DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")

	public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
		movieService.deleteMovie(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")

	public ResponseEntity<List<MovieResponse>> searchMoviesByGenre(@RequestParam String genre) {
		List<MovieResponse> movies = movieService.searchMoviesByGenre(genre);
		return ResponseEntity.ok(movies);
	}

	@GetMapping("/top-rated")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")

	public ResponseEntity<List<MovieResponse>> getTopRatedMovies(@RequestParam double minRating) {
		List<MovieResponse> movies = movieService.getTopRatedMovies(minRating);
		return ResponseEntity.ok(movies);
	}
}
