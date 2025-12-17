package com.exemple.movieapp.controller;

import com.exemple.movieapp.entities.Movie;
import com.exemple.movieapp.entities.User;
import com.exemple.movieapp.service.CategoryService;
import com.exemple.movieapp.service.MovieService;
import com.exemple.movieapp.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final MovieService movieService;
    private final CategoryService categoryService;

    public UserController(UserService userService,
                          MovieService movieService,
                          CategoryService categoryService) {
        this.userService = userService;
        this.movieService = movieService;
        this.categoryService = categoryService;
    }

    // =================== USER HOME ===================
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("topRated", movieService.getTopRatedMovies(5));
        model.addAttribute("latest", movieService.getLatestMovies());
        return "user/home";
    }

    // =================== USER PROFILE ===================
    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        User user = userService.getUserByUsername(auth.getName());
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(Authentication auth,
                                @ModelAttribute User formUser,
                                @RequestParam(value = "password", required = false) String newPassword) {
        User user = userService.getUserByUsername(auth.getName());
        user.setUsername(formUser.getUsername());
        user.setEmail(formUser.getEmail());
        userService.updateProfile(user, newPassword, null);
        return "redirect:/user/home";
    }

    // =================== SEARCH ===================
    @GetMapping("/search")
    public String search(@RequestParam(value = "query", required = false) String query,
                         @RequestParam(value = "categoryId", required = false) Long categoryId,
                         Model model) {

        if (query != null && !query.isEmpty()) {
            model.addAttribute("movies", movieService.searchMovies(query));
        } else if (categoryId != null) {
            model.addAttribute("movies", movieService.filterByCategory(categoryId));
        } else {
            model.addAttribute("movies", movieService.getAllMovies());
        }

        model.addAttribute("categories", categoryService.getAllCategories());
        return "user/search";
    }

    // =================== MOVIE DETAILS ===================
    @GetMapping("/movie/{id}")
    public String movieDetails(@PathVariable Long id, Authentication auth, Model model) {
        Movie movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);
        model.addAttribute("comments", movieService.getCommentsByMovie(id));

        if (auth != null) {
            User user = userService.getUserByUsername(auth.getName());
            model.addAttribute("isFavorite", movieService.isFavorite(user, movie));
        } else {
            model.addAttribute("isFavorite", false);
        }

        return "user/movie-details";
    }

    // =================== ADD COMMENT ===================
    @PostMapping("/movie/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             @RequestParam double rating,  // <-- new
                             Authentication auth) {
        if (auth != null) {
            User user = userService.getUserByUsername(auth.getName());
            Movie movie = movieService.getMovieById(id);
            movieService.addComment(user, movie, content, rating);
        }
        return "redirect:/user/home";
    }


    // =================== ADD/REMOVE FAVORITE ===================
    @PostMapping("/movie/{id}/favorite")
    public String toggleFavorite(@PathVariable Long id, Authentication auth) {
        if (auth != null) {
            User user = userService.getUserByUsername(auth.getName());
            Movie movie = movieService.getMovieById(id);
            if (movieService.isFavorite(user, movie)) {
                movieService.removeFavorite(user, movie);
            } else {
                movieService.addFavorite(user, movie);
            }
        }
        return "redirect:/user/movie/" + id;
    }

    // =================== USER FAVORITES ===================
    @GetMapping("/favorites")
    public String favorites(Authentication auth, Model model) {
        if (auth != null) {
            User user = userService.getUserByUsername(auth.getName());
            model.addAttribute("movies", movieService.getFavoritesByUser(user));
        }
        return "user/favorites";
    }
}
