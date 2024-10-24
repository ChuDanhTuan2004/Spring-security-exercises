package com.example.jwtspring3.model.library;

import com.example.jwtspring3.model.User;
import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.*;

import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Wishlist {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

            @OneToOne
            private User user;

            @ManyToMany
            private Set<Book> books;
}
