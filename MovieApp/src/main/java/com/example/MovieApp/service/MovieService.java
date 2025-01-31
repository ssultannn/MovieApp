package com.example.MovieApp.service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.MovieApp.entity.MovieEntity;
import com.example.MovieApp.exception.MovieNotFoundException;
import com.example.MovieApp.repository.MovieRepository;
import com.example.MovieApp.request.MovieRequest;
import com.example.MovieApp.response.MovieResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {
	@Autowired
	private MovieRepository movieRepository;
	  public MovieResponse createMovie(MovieRequest movieRequest) {
	        validateMovieRequest(movieRequest);

	        MovieEntity movie = new MovieEntity();
	        movie.setTitle(movieRequest.getTitle());
	        movie.setDirector(movieRequest.getDirector());
	        movie.setReleaseYear(movieRequest.getReleaseYear());
	        movie.setGenre(movieRequest.getGenre());
	        movie.setImdbRating(movieRequest.getImdbRating());

	        MovieEntity savedMovie = movieRepository.save(movie);
	        return mapToResponse(savedMovie);
	    }

	    public List<MovieResponse> getAllMovies() {
	        return movieRepository.findAll().stream()
	                .map(this::mapToResponse)
	                .collect(Collectors.toList());
	    }

	    public MovieResponse getMovieById(Long id) {
	        MovieEntity movie = movieRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
	        return mapToResponse(movie);
	    }

	    public MovieResponse updateMovie(Long id, MovieRequest movieRequest) {
	        validateMovieRequest(movieRequest);

	        MovieEntity movie = movieRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));

	        movie.setTitle(movieRequest.getTitle());
	        movie.setDirector(movieRequest.getDirector());
	        movie.setReleaseYear(movieRequest.getReleaseYear());
	        movie.setGenre(movieRequest.getGenre());
	        movie.setImdbRating(movieRequest.getImdbRating());

	        MovieEntity updatedMovie = movieRepository.save(movie);
	        return mapToResponse(updatedMovie);
	    }

	    public void deleteMovie(Long id) {
	        if (!movieRepository.existsById(id)) {
	            throw new RuntimeException("Movie not found with id: " + id);
	        }
	        movieRepository.deleteById(id);
	    }

	    public List<MovieResponse> searchMoviesByGenre(String genre) {
	        return movieRepository.findByGenreContainingIgnoreCase(genre)
	                .stream()
	                .map(this::mapToResponse)
	                .collect(Collectors.toList());
	    }

	    public List<MovieResponse> getTopRatedMovies(double minRating) {
	        return movieRepository.findByImdbRatingGreaterThanEqual(minRating)
	                .stream()
	                .map(this::mapToResponse)
	                .collect(Collectors.toList());
	    }

	    private MovieResponse mapToResponse(MovieEntity movie) {
	        return new MovieResponse(
	                movie.getId(),
	                movie.getTitle(),
	                movie.getDirector(),
	                Optional.ofNullable(movie.getReleaseYear()).orElse(0),
	                movie.getGenre(),
	                Optional.ofNullable(movie.getImdbRating()).orElse(0.0d)
	        );
	    }

	    private void validateMovieRequest(MovieRequest movieRequest) {
	        if (movieRequest.getTitle() == null || movieRequest.getTitle().isEmpty()) {
	            throw new IllegalArgumentException("Title cannot be null or empty");
	        }
	        if (movieRequest.getDirector() == null || movieRequest.getDirector().isEmpty()) {
	            throw new IllegalArgumentException("Director cannot be null or empty");
	        }
	        // Add other validations as needed
	    }

}
