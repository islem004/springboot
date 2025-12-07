package com.exemple.movieapp.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private double rating; // VERY IMPORTANT

    private String imageName;
    private String description;



    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
