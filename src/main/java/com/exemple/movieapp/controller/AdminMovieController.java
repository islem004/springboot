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
import java.util.List;

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

    // ================= LIST =================
    @GetMapping
    public String listMovies(Model model) {
        List<Movie> movies = movieService.getAllMovies();
        System.out.println("MOVIES SIZE = " + movies.size());
        for (Movie m : movies) {
            System.out.println("Movie: " + m.getTitle() + ", Category: " + (m.getCategory() != null ? m.getCategory().getName() : "NULL"));
        }
        model.addAttribute("movies", movies);
        return "admin/movies";
    }


    // ================= ADD =================
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("movie", new Movie());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/add-movie";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Movie movie,
                       @RequestParam Long categoryId,
                       @RequestParam("imageFile") MultipartFile file) throws IOException {

        handleImage(movie, file);
        movie.setCategory(categoryService.getCategoryById(categoryId));
        movieService.saveMovie(movie);

        return "redirect:/admin/movies";
    }

    // ================= EDIT =================
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieService.getMovieById(id));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/edit-movie";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute Movie movie,
                         @RequestParam Long categoryId,
                         @RequestParam("imageFile") MultipartFile file) throws IOException {

        Movie existing = movieService.getMovieById(id);

        handleImage(movie, file);
        if (movie.getImageUrl() == null) {
            movie.setImageUrl(existing.getImageUrl());
        }

        movie.setId(id);
        movie.setCategory(categoryService.getCategoryById(categoryId));
        movieService.saveMovie(movie);

        return "redirect:/admin/movies";
    }

    // ================= DELETE =================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/admin/movies";
    }

    // ================= IMAGE =================
    private void handleImage(Movie movie, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File dir = new File("src/main/resources/static/images/movies/");
            if (!dir.exists()) dir.mkdirs();

            file.transferTo(new File(dir, fileName));
            movie.setImageUrl("/images/movies/" + fileName);
        }
    }
}
