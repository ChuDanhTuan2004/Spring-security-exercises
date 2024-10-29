package com.example.jwtspring3.request;

import com.example.jwtspring3.model.library.enumeration.RequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookAccessRequestDetailDTO {
    private Long id;
    private String username;
    private String bookTitle;
    private RequestStatus status;
    private LocalDateTime requestDate;
    private String reason;
}
