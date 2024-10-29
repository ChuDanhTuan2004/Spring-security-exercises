package com.example.jwtspring3.repository.library;

import com.example.jwtspring3.model.User;
import com.example.jwtspring3.model.library.Book;
import com.example.jwtspring3.model.library.BookAccessRequest;
import com.example.jwtspring3.model.library.enumeration.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookAccessRequestRepository extends JpaRepository<BookAccessRequest, Long>, JpaSpecificationExecutor<BookAccessRequest> {
    List<BookAccessRequest> findByUserOrderByRequestDateDesc(User user);

    List<BookAccessRequest> findByStatusOrderByRequestDateDesc(RequestStatus status);

    Optional<BookAccessRequest> findByUserAndBookAndStatus(User user, Book book, RequestStatus status);

    Page<BookAccessRequest> findByStatusOrderByRequestDateDesc(RequestStatus status, Pageable pageable);

    @Query("SELECT r FROM BookAccessRequest r " +
            "JOIN r.user u " +
            "JOIN r.book b " +
            "WHERE (:status IS NULL OR r.status = :status) " +
            "AND (:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) " +
            "AND (:bookTitle IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :bookTitle, '%')))")
    Page<BookAccessRequest> findAllWithFilters(
            @Param("status") RequestStatus status,
            @Param("username") String username,
            @Param("bookTitle") String bookTitle,
            Pageable pageable);

}
