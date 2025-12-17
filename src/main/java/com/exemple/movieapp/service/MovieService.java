package com.exemple.movieapp.service;

import com.exemple.movieapp.entities.Comment;
import com.exemple.movieapp.entities.Favorite;
import com.exemple.movieapp.entities.Movie;
import com.exemple.movieapp.entities.User;
import com.exemple.movieapp.repository.CommentRepository;
import com.exemple.movieapp.repository.FavoriteRepository;
import com.exemple.movieapp.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final CommentRepository commentRepository;
    private final FavoriteRepository favoriteRepository;

    public MovieService(MovieRepository movieRepository,
                        CommentRepository commentRepository,
                        FavoriteRepository favoriteRepository) {
        this.movieRepository = movieRepository;
        this.commentRepository = commentRepository;
        this.favoriteRepository = favoriteRepository;
    }

    // =================== Movie Methods ===================
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public List<Movie> searchMovies(String keyword) {
        return movieRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Movie> filterByCategory(Long categoryId) {
        return movieRepository.findByCategoryId(categoryId);
    }

    public List<Movie> getTopRatedMovies(int limit) {
        return movieRepository.findTop5ByOrderByRatingDesc();
    }

    public List<Movie> getLatestMovies() {
        LocalDate startOfYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        return movieRepository.findByReleaseDateAfterOrderByReleaseDateDesc(startOfYear);
    }


    // =================== Comment Methods ===================
    public List<Comment> getCommentsByMovie(Long movieId) {
        return commentRepository.findByMovieIdOrderByCreatedAtDesc(movieId);
    }

    public void addComment(User user, Movie movie, String content, double userRating) {
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setMovie(movie);
        comment.setContent(content);
        comment.setRating(userRating);  // save user rating
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        // Update movie average rating
        List<Comment> comments = movie.getComments();
        comments.add(comment);  // include the new comment
        double average = comments.stream()
                .mapToDouble(Comment::getRating)
                .average()
                .orElse(0.0);
        movie.setRating(average);
        movieRepository.save(movie);
    }


    // =================== Favorite Methods ===================
    public void addFavorite(User user, Movie movie) {
        if (favoriteRepository.findByUserAndMovie(user, movie).isEmpty()) {
            Favorite fav = new Favorite();
            fav.setUser(user);
            fav.setMovie(movie);
            favoriteRepository.save(fav);
        }
    }

    public void removeFavorite(User user, Movie movie) {
        favoriteRepository.findByUserAndMovie(user, movie)
                .ifPresent(favoriteRepository::delete);
    }

    public boolean isFavorite(User user, Movie movie) {
        return favoriteRepository.findByUserAndMovie(user, movie).isPresent();
    }

    public List<Movie> getFavoritesByUser(User user) {
        return favoriteRepository.findByUser(user).stream()
                .map(Favorite::getMovie)
                .toList();
    }
}
