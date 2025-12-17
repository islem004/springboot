package com.exemple.movieapp.controller;

import com.exemple.movieapp.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/comments")
public class AdminCommentController {

    private final CommentService commentService;

    public AdminCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // ================= LIST =================
    @GetMapping
    public String list(Model model) {
        model.addAttribute("comments", commentService.getAllComments());
        return "admin/comments";
    }

    // ================= DELETE =================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        commentService.deleteComment(id);
        return "redirect:/admin/comments";
    }
}
