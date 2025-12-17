package com.exemple.movieapp.controller;

import com.exemple.movieapp.entities.User;
import com.exemple.movieapp.repository.UserRepository;
import com.exemple.movieapp.service.UserService;
import com.exemple.movieapp.service.MovieService;
import com.exemple.movieapp.service.CategoryService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final MovieService movieService;
    private final CategoryService categoryService;

    public UserController(UserService userService,
                          UserRepository userRepository,
                          MovieService movieService,
                          CategoryService categoryService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.movieService = movieService;
        this.categoryService = categoryService;
    }

    // =================== USER HOME ===================
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("topRated", movieService.getTopRatedMovies(5));
        model.addAttribute("latest", movieService.getLatestMovies(5));
        return "user/home"; // matches user/home.html
    }

    // =================== USER PROFILE ===================
    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        model.addAttribute("user", user);
        return "user/profile"; // matches user/profile.html
    }

    @PostMapping("/profile")
    public String updateProfile(Authentication auth,
                                @ModelAttribute User formUser,
                                @RequestParam(value = "password", required = false) String newPassword) {

        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        // Update user fields
        user.setUsername(formUser.getUsername());
        user.setEmail(formUser.getEmail());

        // Update password if provided
        userService.updateProfile(user, newPassword, null);

        // Redirect to home page
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
        return "user/search"; // create user/search.html page
    }
}
