package com.example.jwtspring3.service.library;

import com.example.jwtspring3.model.library.BookAccessRequest;

public interface BookAccessService {
    BookAccessRequest createAccessRequest(Long userId, Long bookId, String reason);
    BookAccessRequest processRequest(Long requestId, Long librarianId, boolean approved, String rejectionReason);
    boolean checkAccessPermission(Long userId, Long bookId);
}
