package com.exemple.movieapp.repository;

import com.exemple.movieapp.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByCategoryId(Long categoryId);
    List<Movie> findTop5ByOrderByRatingDesc();
    List<Movie> findByReleaseDateAfterOrderByReleaseDateDesc(LocalDate date);

    List<Movie> findAllByOrderByReleaseDateDesc();
}

