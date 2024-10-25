package com.example.jwtspring3.service.library.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAccessRequestDTO {
    private Long userId;
    private Long bookId;
    private String reason;
}
