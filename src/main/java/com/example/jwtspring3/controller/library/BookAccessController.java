package com.example.jwtspring3.controller.library;

import com.example.jwtspring3.model.library.BookAccessRequest;
import com.example.jwtspring3.service.library.BookAccessService;
import com.example.jwtspring3.service.library.dto.BookAccessRequestDTO;
import com.example.jwtspring3.service.library.dto.ProcessRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book-access")
@CrossOrigin("*")
public class BookAccessController {
    private final BookAccessService bookAccessService;

    @Autowired
    public BookAccessController(BookAccessService bookAccessService) {
        this.bookAccessService = bookAccessService;
    }

    @PostMapping("/request")
    public ResponseEntity<BookAccessRequest> createRequest(
            @RequestBody BookAccessRequestDTO requestDTO) {
        BookAccessRequest request = bookAccessService.createAccessRequest(
                requestDTO.getUserId(),
                requestDTO.getBookId(),
                requestDTO.getReason()
        );
        return ResponseEntity.ok(request);
    }

    @PostMapping("/process/{requestId}")
    public ResponseEntity<BookAccessRequest> processRequest(
            @PathVariable Long requestId,
            @RequestBody ProcessRequestDTO processDTO) {
        BookAccessRequest request = bookAccessService.processRequest(
                requestId,
                processDTO.getLibrarianId(),
                processDTO.isApproved(),
                processDTO.getRejectionReason()
        );
        return ResponseEntity.ok(request);
    }

    @GetMapping("/check/{userId}/{bookId}")
    public ResponseEntity<Boolean> checkAccess(
            @PathVariable Long userId,
            @PathVariable Long bookId) {
        boolean hasAccess = bookAccessService.checkAccessPermission(userId, bookId);
        return ResponseEntity.ok(hasAccess);
    }
}