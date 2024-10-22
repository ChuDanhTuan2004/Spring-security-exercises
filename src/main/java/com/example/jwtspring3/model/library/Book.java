package com.example.jwtspring3.model.library;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;
    private String title;
    private String author;
    private String publisher;
    private int publishYear;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    private String description;
    private String imageUrl;
    private String url;
}
