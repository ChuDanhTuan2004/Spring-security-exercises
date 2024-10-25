package com.example.jwtspring3.service.library.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessRequestDTO {
    private Long librarianId;
    private boolean approved;
    private String rejectionReason;
}
