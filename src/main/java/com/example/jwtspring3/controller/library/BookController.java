package com.example.jwtspring3.controller.library;

import com.example.jwtspring3.model.library.Book;
import com.example.jwtspring3.request.BookRequest;
import com.example.jwtspring3.request.PaginateRequest;
import com.example.jwtspring3.service.UserService;
import com.example.jwtspring3.service.library.BookService;
import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin("*")
@RequestMapping("/books")
@MultipartConfig(location = "/src/main/resources/static/bookImages/", maxFileSize = 1024 * 1024 * 5) // 5MB
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<Page<Book>> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) Integer publishYear,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle(title);
        bookRequest.setAuthor(author);
        bookRequest.setPublisher(publisher);
        if (publishYear != null) {
            bookRequest.setPublishYear(publishYear);
        }
        bookRequest.setCategoryId(categoryId);

        PaginateRequest paginateRequest = new PaginateRequest(page, size);
        Page<Book> paginatedBooks = bookService.findAll(bookRequest, paginateRequest);


        if (paginatedBooks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(paginatedBooks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("")
    public ResponseEntity<?> createBook(@RequestBody BookRequest book) {
        try {
            Book savedBook = bookService.createBookWithDefaultImage(book);
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error: Category does not exist. Please provide a valid category ID.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookRequest book) {
        try {
            Book updatedBook = bookService.updateBook(id, book);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Book not found with id: " + id);
        }
    }

    private void updateBookFields(Book existingBook, Book updatedBook) {
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setPublisher(updatedBook.getPublisher());
        existingBook.setPublishYear(updatedBook.getPublishYear());
        existingBook.setQuantity(updatedBook.getQuantity());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok("Book deleted successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Book not found with id: " + id);
        }
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestPart("file") MultipartFile file, @RequestParam("bookId") Long bookId) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload");
        }

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("Vui lòng chọn một tệp để tải lên");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return ResponseEntity.badRequest().body("Tên tệp là null");
        }

        String fileName = StringUtils.cleanPath(originalFilename);
        String uploadDir = "src/main/resources/static/bookImages/";
        String fileUrl = "bookImages/" + fileName;

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            // Update book's imageUrl
            Book book = bookService.getBookById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
            book.setImageUrl(fileUrl);

//            bookService.saveBook(book);

            return ResponseEntity.ok().body("File uploaded successfully. URL: " + fileUrl);
        } catch (IOException e) {
            throw new IOException("Could not save file: " + fileName, e);
        }
    }
}