package com.exemple.movieapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    @Column(length = 500)
    private String imageUrl; // can store file path or online URL




    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
