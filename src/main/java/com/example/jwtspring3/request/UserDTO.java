package com.example.jwtspring3.request;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
}
