package com.example.jwtspring3.service;


import com.example.jwtspring3.model.User;
import com.example.jwtspring3.request.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    void save(User user);

    Iterable<User> findAll();

    User findByUsername(String username);

    User getCurrentUser();

    Optional<User> findById(Long id);

    UserDetails loadUserById(Long id);

    boolean checkLogin(User user);

    boolean isRegister(User user);

    boolean isCorrectConfirmPassword(User user);

    void deleteUser(Long id);

    List<User> searchByName(String name);

    UserDTO getUserByUsername(String username);

    List<User> findUsersAppend();

    void acceptUser(Long id);
    Page<User> findPendingRegistrations(int page, int size, String search);
}
