package com.example.jwtspring3.service.library;

import com.example.jwtspring3.model.library.Wishlist;

import java.util.List;
import java.util.Optional;

public interface WishlistService {
    Wishlist createWishlist(Wishlist wishlist);
    List<Wishlist> getAllWishlists();
    Optional<Wishlist> getWishlistById(Long id);
    Wishlist updateWishlist(Long id, Wishlist wishlist);
    void deleteWishlist(Long id);
    void addBookToWishlist(Long userId, Long bookId);
    void removeBookFromWishlist(Long userId, Long bookId);
    Wishlist getWishlistByUserId(Long userId);
}
