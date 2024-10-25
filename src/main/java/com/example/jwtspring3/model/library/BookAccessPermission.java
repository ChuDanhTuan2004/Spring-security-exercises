package com.example.jwtspring3.model.library;

import com.example.jwtspring3.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_access_permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAccessPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private LocalDateTime grantedDate;
    private LocalDateTime expiryDate;
    @Column(name = "active")
    private boolean active;
}