package com.exemple.movieapp.controller;

import com.exemple.movieapp.entities.User;
import com.exemple.movieapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    // ================= LIST =================
    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    // ================= EDIT =================
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "admin/edit-user";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute User user) {
        user.setId(id);
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    // ================= DELETE =================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
