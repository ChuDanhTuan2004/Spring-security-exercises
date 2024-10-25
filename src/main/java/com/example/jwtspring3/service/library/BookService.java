package com.example.jwtspring3.service.library;

import com.example.jwtspring3.model.library.Book;
import com.example.jwtspring3.request.BookRequest;
import com.example.jwtspring3.request.PaginateRequest;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface BookService {
    Optional<Book> getBookById(Long id);
    Book saveBook(BookRequest book);
    void deleteBook(Long id);

    Book createBookWithDefaultImage(BookRequest book);

    Page<Book> findAll(BookRequest roomRequest, PaginateRequest paginateRequest);

    Book updateBook(Long id, BookRequest book);
}
