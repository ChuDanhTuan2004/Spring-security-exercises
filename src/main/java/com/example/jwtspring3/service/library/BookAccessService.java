package com.example.jwtspring3.service.library;

import com.example.jwtspring3.model.library.BookAccessRequest;
import com.example.jwtspring3.model.library.enumeration.RequestStatus;
import com.example.jwtspring3.request.BookAccessRequestDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookAccessService {
    BookAccessRequest createAccessRequest(Long userId, Long bookId, String reason);
    BookAccessRequest processRequest(Long requestId, Long librarianId, boolean approved, String rejectionReason);
    boolean checkAccessPermission(Long userId, Long bookId);
    Page<BookAccessRequest> getPendingRequests(int page, int size);
    Page<BookAccessRequestDetailDTO> getAllRequestsWithDetails(Pageable pageable, RequestStatus status, String username, String bookTitle);
}
