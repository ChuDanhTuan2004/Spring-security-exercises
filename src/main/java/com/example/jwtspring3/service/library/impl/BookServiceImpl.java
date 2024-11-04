package com.example.jwtspring3.service.library.impl;

import com.example.jwtspring3.model.library.Book;
import com.example.jwtspring3.model.library.Category;
import com.example.jwtspring3.repository.library.BookRepository;
import com.example.jwtspring3.repository.library.CategoryRepository;
import com.example.jwtspring3.request.BookRequest;
import com.example.jwtspring3.request.BookRequestSpecification;
import com.example.jwtspring3.request.PaginateRequest;
import com.example.jwtspring3.service.UserService;
import com.example.jwtspring3.service.library.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private static final String DEFAULT_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/demofirebase-6e7a1.appspot.com/o/images%2Flogo%20hoa%20thanh%20do.png?alt=media&token=undefined";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Book saveBook(BookRequest book) {
        Book newBook = new Book();
        if (book.getCategoryId() != null) {
            Category category = categoryRepository.findById(book.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
            newBook.setCategory(category);
        }
        newBook.setAuthor(book.getAuthor());
        newBook.setTitle(book.getTitle());
        newBook.setPublisher(book.getPublisher());
        newBook.setPublishYear(book.getPublishYear());
        newBook.setQuantity(book.getQuantity());
        newBook.setImageUrl(book.getImageUrl()); // Set the image URL if provided
        newBook.setDescription(book.getDescription());
        newBook.setUrl(book.getUrl());
        return bookRepository.save(newBook);
    }

    @Override
    public Book createBookWithDefaultImage(BookRequest book) {
        Book newBook = new Book();
        if (book.getCategoryId() != null) {
            Category category = categoryRepository.findById(book.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
            newBook.setCategory(category);
        }
        newBook.setAuthor(book.getAuthor());
        newBook.setTitle(book.getTitle());
        newBook.setPublisher(book.getPublisher());
        newBook.setPublishYear(book.getPublishYear());
        newBook.setQuantity(book.getQuantity());
        newBook.setImageUrl(book.getImageUrl() != null && !book.getImageUrl().isEmpty()
                ? book.getImageUrl()
                : DEFAULT_IMAGE_URL); // Set default image URL if not provided
        newBook.setDescription(book.getDescription());
        newBook.setUrl(book.getUrl());
        return bookRepository.save(newBook);
    }


    @Override
    public Page<Book> findAll(BookRequest bookRequest, PaginateRequest paginateRequest) {
        Specification<Book> specification = new BookRequestSpecification(bookRequest);
        Pageable pageable = PageRequest.of(paginateRequest.getPage(), paginateRequest.getSize());
        Page<Book> bookPage = bookRepository.findAll(specification, pageable);
        System.out.println("curent user"+userService.getCurrentUser().toString());
        if (!userService.getCurrentUser().getRoles().stream().findFirst().get().getName().equals("ROLE_ADMIN")) {
            List<Book> books = bookPage.getContent();
            books.forEach(book -> book.setUrl(null));
            return new PageImpl<>(books, pageable, bookPage.getTotalElements());
        }
        return bookPage;
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NoSuchElementException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public Book updateBook(Long id, BookRequest book) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + id));

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPublisher(book.getPublisher());
        existingBook.setPublishYear(book.getPublishYear());
        existingBook.setQuantity(book.getQuantity());
        existingBook.setImageUrl(book.getImageUrl()); // Update the image URL if provided

        if (book.getCategoryId() != null) {
            Category category = categoryRepository.findById(book.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
            existingBook.setCategory(category);
        }

        return bookRepository.save(existingBook);
    }
}
