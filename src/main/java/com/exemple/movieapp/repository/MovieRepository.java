package com.exemple.movieapp.repository;

import com.exemple.movieapp.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByCategoryId(Long categoryId);
}

