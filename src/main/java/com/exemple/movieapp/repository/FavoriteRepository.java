package com.exemple.movieapp.repository;

import com.exemple.movieapp.entities.Favorite;
import com.exemple.movieapp.entities.Movie;
import com.exemple.movieapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserAndMovie(User user, Movie movie);
    List<Favorite> findByUser(User user);
}
