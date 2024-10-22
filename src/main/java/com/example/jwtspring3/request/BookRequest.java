package com.example.jwtspring3.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    private String title;
    private String author;
    private String publisher;
    private int publishYear;
    private int quantity;
    private Long categoryId;
}
