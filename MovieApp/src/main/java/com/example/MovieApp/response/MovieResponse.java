package com.example.MovieApp.response;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieResponse {
    private Long id;
    private String title;
    private String director;
    private int releaseYear;
    private String genre;
    private Double imdbRating;
}

