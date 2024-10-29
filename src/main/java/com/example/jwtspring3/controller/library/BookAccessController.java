package com.example.jwtspring3.controller.library;

import com.example.jwtspring3.model.library.BookAccessRequest;
import com.example.jwtspring3.model.library.enumeration.RequestStatus;
import com.example.jwtspring3.request.BookAccessRequestDetailDTO;
import com.example.jwtspring3.service.library.BookAccessService;
import com.example.jwtspring3.service.library.dto.BookAccessRequestDTO;
import com.example.jwtspring3.service.library.dto.ProcessRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-access")
@CrossOrigin("*")
public class BookAccessController {
    private final BookAccessService bookAccessService;

    @Autowired
    public BookAccessController(BookAccessService bookAccessService) {
        this.bookAccessService = bookAccessService;
    }

    @GetMapping("/pending-requests")
    public ResponseEntity<List<BookAccessRequest>> getPendingRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BookAccessRequest> pendingRequests = bookAccessService.getPendingRequests(page, size);
        return ResponseEntity.ok(pendingRequests.getContent());
    }

    @GetMapping("/requests")
    public ResponseEntity<Page<BookAccessRequestDetailDTO>> getAllRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) RequestStatus status,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String bookTitle
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookAccessRequestDetailDTO> requests = bookAccessService.getAllRequestsWithDetails(pageable, status, username, bookTitle);
        return ResponseEntity.ok(requests);
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