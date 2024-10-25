package com.example.jwtspring3.repository.library;

import com.example.jwtspring3.model.User;
import com.example.jwtspring3.model.library.Book;
import com.example.jwtspring3.model.library.BookAccessPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookAccessPermissionRepository extends JpaRepository<BookAccessPermission, Long> {
    Optional<BookAccessPermission> findByUserAndBookAndActiveIs(User user, Book book, boolean isActive);
}
