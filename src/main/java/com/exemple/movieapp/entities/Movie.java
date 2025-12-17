package com.exemple.movieapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private double rating;
    private LocalDate releaseDate;
    private String description;
    private String director;
    @Column(length = 1000)
    private String cast;
    @Column(length = 500)
    private String imageUrl;
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Favorite> favorites = new ArrayList<>();
// can store file path or online URL




    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
