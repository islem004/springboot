package com.exemple.movieapp.controller;

import com.exemple.movieapp.entities.Category;
import com.exemple.movieapp.entities.Movie;
import com.exemple.movieapp.service.CategoryService;
import com.exemple.movieapp.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/admin/movies")
public class AdminMovieController {

    private final MovieService movieService;
    private final CategoryService categoryService;

    public AdminMovieController(MovieService movieService,
                                CategoryService categoryService) {
        this.movieService = movieService;
        this.categoryService = categoryService;
    }

    // -------------------- List all movies --------------------
    @GetMapping
    public String listMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "admin/movies";
    }

    // -------------------- Show add form --------------------
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("movie", new Movie());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/add-movie";
    }

    // -------------------- Save new movie --------------------
    @PostMapping("/save")
    public String save(
            @ModelAttribute Movie movie,
            @RequestParam Long categoryId,
            @RequestParam("imageFile") MultipartFile file,
            @RequestParam("imageUrl") String imageUrl) throws IOException {

        // Priority: uploaded file first, then URL if file is empty
        if (!file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String uploadDir = "src/main/resources/static/images/movies/";
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) uploadPath.mkdirs();
            file.transferTo(new File(uploadDir + fileName));
            movie.setImageUrl("/images/movies/" + fileName);
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            movie.setImageUrl(imageUrl);
        }

        // Set category
        Category category = categoryService.getCategoryById(categoryId);
        movie.setCategory(category);

        movieService.saveMovie(movie);
        return "redirect:/admin/movies";
    }

    // -------------------- Show edit form --------------------
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/edit-movie";
    }

    // -------------------- Update movie --------------------
    @PostMapping("/update/{id}")
    public String updateMovie(
            @PathVariable Long id,
            @ModelAttribute Movie movie,
            @RequestParam Long categoryId,
            @RequestParam("imageFile") MultipartFile file,
            @RequestParam("imageUrl") String imageUrl) throws IOException {

        if (!file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String uploadDir = "src/main/resources/static/images/movies/";
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) uploadPath.mkdirs();
            file.transferTo(new File(uploadDir + fileName));
            movie.setImageUrl("/images/movies/" + fileName);
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            movie.setImageUrl(imageUrl);
        } else {
            // Keep existing image if no new file or URL is provided
            Movie existing = movieService.getMovieById(id);
            movie.setImageUrl(existing.getImageUrl());
        }

        Category category = categoryService.getCategoryById(categoryId);
        movie.setCategory(category);
        movie.setId(id);
        movieService.saveMovie(movie);

        return "redirect:/admin/movies";
    }

    // -------------------- Delete movie --------------------
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/admin/movies";
    }
}
