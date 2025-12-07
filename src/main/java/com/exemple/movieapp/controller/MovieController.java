package com.exemple.movieapp.controller;

import com.exemple.movieapp.entities.Category;
import com.exemple.movieapp.entities.Movie;
import com.exemple.movieapp.service.CategoryService;
import com.exemple.movieapp.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final CategoryService categoryService;

    public MovieController(MovieService movieService, CategoryService categoryService) {
        this.movieService = movieService;
        this.categoryService = categoryService;
    }

    // List movies with optional search and filter
    @GetMapping
    public String listMovies(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            Model model
    ) {
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("movies", movieService.searchMovies(keyword));
        } else if (categoryId != null) {
            model.addAttribute("movies", movieService.filterByCategory(categoryId));
        } else {
            model.addAttribute("movies", movieService.getAllMovies());
        }

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);

        return "movies";
    }

    // Add movie form
    @GetMapping("/add")
    public String addMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "add-movie";
    }

    // Save movie
    @PostMapping("/save")
    public String saveMovie(
            @ModelAttribute Movie movie,
            @RequestParam Long categoryId
    ) {
        Category category = categoryService.getCategoryById(categoryId);
        movie.setCategory(category);

        movieService.saveMovie(movie);
        return "redirect:/movies";
    }

    // Edit movie form
    @GetMapping("/edit/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieById(id);
        if (movie == null) return "redirect:/movies";
        model.addAttribute("movie", movie);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit-movie";
    }

    // Update movie
    @PostMapping("/update/{id}")
    public String updateMovie(
            @PathVariable Long id,
            @ModelAttribute Movie movie,
            @RequestParam Long categoryId
    ) {
        Movie existingMovie = movieService.getMovieById(id);
        if (existingMovie == null) return "redirect:/movies";

        existingMovie.setTitle(movie.getTitle());
        existingMovie.setRating(movie.getRating());
        existingMovie.setDescription(movie.getDescription());

        Category category = categoryService.getCategoryById(categoryId);
        existingMovie.setCategory(category);

        movieService.saveMovie(existingMovie);
        return "redirect:/movies";
    }

    // Delete movie
    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/movies";
    }
}
