package com.exemple.movieapp.repository;

import com.exemple.movieapp.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMovieIdOrderByCreatedAtDesc(Long movieId);
}