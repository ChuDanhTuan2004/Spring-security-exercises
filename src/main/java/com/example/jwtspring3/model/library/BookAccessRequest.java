package com.example.jwtspring3.model.library;

import com.example.jwtspring3.model.User;
import com.example.jwtspring3.model.library.enumeration.RequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_access_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAccessRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDateTime requestDate;
    private LocalDateTime processedDate;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    private User processedBy;

    private String reason;
    private String rejectionReason;
}
