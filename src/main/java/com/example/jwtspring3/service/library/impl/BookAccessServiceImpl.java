package com.example.jwtspring3.service.library.impl;

import com.example.jwtspring3.exception.ResourceNotFoundException;
import com.example.jwtspring3.model.User;
import com.example.jwtspring3.model.library.*;
import com.example.jwtspring3.model.library.enumeration.NotificationType;
import com.example.jwtspring3.model.library.enumeration.RequestStatus;
import com.example.jwtspring3.repository.*;
import com.example.jwtspring3.repository.library.BookAccessPermissionRepository;
import com.example.jwtspring3.repository.library.BookAccessRequestRepository;
import com.example.jwtspring3.repository.library.BookRepository;
import com.example.jwtspring3.repository.library.NotificationRepository;
import com.example.jwtspring3.request.BookAccessRequestDetailDTO;
import com.example.jwtspring3.service.library.BookAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class BookAccessServiceImpl implements BookAccessService {
    private final BookAccessRequestRepository requestRepository;
    private final BookAccessPermissionRepository permissionRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookAccessServiceImpl(BookAccessRequestRepository requestRepository,
                                 BookAccessPermissionRepository permissionRepository,
                                 NotificationRepository notificationRepository,
                                 UserRepository userRepository,
                                 BookRepository bookRepository) {
        this.requestRepository = requestRepository;
        this.permissionRepository = permissionRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public BookAccessRequest createAccessRequest(Long userId, Long bookId, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        // Check if user already has active permission
        Optional<BookAccessPermission> existingPermission =
                permissionRepository.findByUserAndBookAndActiveIs(user, book, true);
        if (existingPermission.isPresent()) {
            throw new IllegalStateException("User already has access to this book");
        }

        // Check if there's already a pending request
        Optional<BookAccessRequest> pendingRequest =
                requestRepository.findByUserAndBookAndStatus(user, book, RequestStatus.PENDING);
        if (pendingRequest.isPresent()) {
            throw new IllegalStateException("User already has a pending request for this book");
        }

        BookAccessRequest request = new BookAccessRequest();
        request.setUser(user);
        request.setBook(book);
        request.setStatus(RequestStatus.PENDING);
        request.setReason(reason);
        request.setRequestDate(LocalDateTime.now());

        return requestRepository.save(request);
    }

    @Override
    public BookAccessRequest processRequest(Long requestId, Long librarianId, boolean approved, String rejectionReason) {
        BookAccessRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        User librarian = userRepository.findById(librarianId)
                .orElseThrow(() -> new ResourceNotFoundException("Librarian not found"));

        request.setProcessedBy(librarian);
        request.setProcessedDate(LocalDateTime.now());
        request.setStatus(approved ? RequestStatus.APPROVED : RequestStatus.REJECTED);

        if (!approved) {
            request.setRejectionReason(rejectionReason);
        } else {
            // Create permission if approved
            BookAccessPermission permission = new BookAccessPermission();
            permission.setUser(request.getUser());
            permission.setBook(request.getBook());
            permission.setGrantedDate(LocalDateTime.now());
            permission.setActive(true);
            permissionRepository.save(permission);
        }

        // Create notification
        Notification notification = new Notification();
        notification.setUser(request.getUser());
        notification.setRequest(request);
        notification.setType(approved ? NotificationType.APPROVAL : NotificationType.REJECTION);
        notification.setMessage(approved ?
                "Your request to access " + request.getBook().getTitle() + " has been approved." :
                "Your request to access " + request.getBook().getTitle() + " has been rejected: " + rejectionReason);
        notificationRepository.save(notification);

        return requestRepository.save(request);
    }

    @Override
    public boolean checkAccessPermission(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        Optional<BookAccessPermission> permission =
                permissionRepository.findByUserAndBookAndActiveIs(user, book, true);
        return permission.isPresent();
    }

    @Override
    public Page<BookAccessRequest> getPendingRequests(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return requestRepository.findByStatusOrderByRequestDateDesc(RequestStatus.PENDING, pageRequest);
    }

    @Override
    public Page<BookAccessRequestDetailDTO> getAllRequestsWithDetails(Pageable pageable, RequestStatus status, String username, String bookTitle) {
        Page<BookAccessRequest> requests = requestRepository.findAllWithFilters(status, username, bookTitle, pageable);
        return requests.map(this::convertToDetailDTO);
    }

    private BookAccessRequestDetailDTO convertToDetailDTO(BookAccessRequest request) {
        BookAccessRequestDetailDTO dto = new BookAccessRequestDetailDTO();
        dto.setId(request.getId());
        dto.setUsername(request.getUser().getUsername());
        dto.setBookTitle(request.getBook().getTitle());
        dto.setStatus(request.getStatus());
        dto.setRequestDate(request.getRequestDate());
        dto.setReason(request.getReason());
        return dto;
    }
}