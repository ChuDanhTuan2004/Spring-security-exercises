package com.example.jwtspring3.repository.library;

import com.example.jwtspring3.model.User;
import com.example.jwtspring3.model.library.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    List<Notification> findByUserAndReadIsOrderByCreatedAtDesc(User user, boolean isRead);
}
