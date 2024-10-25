package com.example.jwtspring3.repository.library;

import com.example.jwtspring3.model.User;
import com.example.jwtspring3.model.library.Book;
import com.example.jwtspring3.model.library.BookAccessRequest;
import com.example.jwtspring3.model.library.enumeration.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookAccessRequestRepository extends JpaRepository<BookAccessRequest, Long> {
    List<BookAccessRequest> findByUserOrderByRequestDateDesc(User user);
    List<BookAccessRequest> findByStatusOrderByRequestDateDesc(RequestStatus status);
    Optional<BookAccessRequest> findByUserAndBookAndStatus(User user, Book book, RequestStatus status);
}
