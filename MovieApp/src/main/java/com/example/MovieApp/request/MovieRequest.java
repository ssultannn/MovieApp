package com.example.MovieApp.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovieRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Director is required")
    private String director;

   
    @Min(value = 1888, message = "Movies didn't exist before 1888!")
    @Max(value = 2100, message = "Release year can't be in the far future")
    private Integer releaseYear;

    @NotBlank(message = "Genre is required")
    private String genre;

   
    @Min(value = 0, message = "Rating cannot be negative")
    @Max(value = 10, message = "Rating cannot exceed 10")
    private Double imdbRating;
}
