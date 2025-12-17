package com.exemple.movieapp.service;

import com.exemple.movieapp.entities.Movie;
import com.exemple.movieapp.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Get all movies
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Get movie by id
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    // Save or update movie
    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }

    // Delete movie by id
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    // Search movies by title (case-insensitive)
    public List<Movie> searchMovies(String keyword) {
        return movieRepository.findByTitleContainingIgnoreCase(keyword);
    }

    // Filter movies by category id
    public List<Movie> filterByCategory(Long categoryId) {
        return movieRepository.findByCategoryId(categoryId);
    }

    // Get top-rated movies (by rating, descending)
    public List<Movie> getTopRatedMovies(int limit) {
        return getAllMovies().stream()

                .sorted(Comparator.comparing(Movie::getRating).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Get latest movies (by releaseDate, descending)
    public List<Movie> getLatestMovies(int limit) {
        return getAllMovies().stream()
                .filter(m -> m.getReleaseDate() != null)
                .sorted(Comparator.comparing(Movie::getReleaseDate).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
