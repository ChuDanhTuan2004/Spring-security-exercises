package com.example.jwtspring3.repository;



import com.example.jwtspring3.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findByUsernameContainingIgnoreCase(String name);
    List<User> findUsersByEnabled(boolean enabled);
    Page<User> findByEnabledFalse(Pageable pageable);
    Page<User> findByEnabledFalseAndUsernameContaining(String username, Pageable pageable);
}