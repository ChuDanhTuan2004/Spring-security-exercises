package com.example.jwtspring3.service.library.impl;

import com.example.jwtspring3.model.User;
import com.example.jwtspring3.model.library.Book;
import com.example.jwtspring3.model.library.Wishlist;
import com.example.jwtspring3.repository.UserRepository;
import com.example.jwtspring3.repository.library.BookRepository;
import com.example.jwtspring3.repository.library.WishlistRepository;
import com.example.jwtspring3.service.library.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public Wishlist createWishlist(Wishlist wishlist) {
        return wishlistRepository.save(wishlist);
    }

    @Override
    public List<Wishlist> getAllWishlists() {
        return wishlistRepository.findAll();
    }

    @Override
    public Wishlist addBookToWishlist(Long userId, Long bookId) {
        Optional<Wishlist> wishlist = wishlistRepository.findByUserId(userId);
        if (wishlist.isPresent()) {
            Optional<Book> book = bookRepository.findById(bookId);
            if (book.isPresent()) {
                wishlist.get().getBooks().add(book.get());
                return wishlistRepository.save(wishlist.get());
            }
        } else {
            Wishlist newWishlist = new Wishlist();
            newWishlist.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId)));
            Optional<Book> book = bookRepository.findById(bookId);
            if (book.isPresent()) {
                newWishlist.getBooks().add(book.get());
                return wishlistRepository.save(newWishlist);
            } else {
                throw new RuntimeException("Book not found with id: " + bookId);
            }
        }
        return new Wishlist();
    }


    @Override
    public void removeBookFromWishlist(Long userId, Long bookId) {
        Optional<Wishlist> wishlist = wishlistRepository.findByUserId(userId);
        if (wishlist.isPresent()) {
            Optional<Book> book = bookRepository.findById(bookId);
            if (book.isPresent()) {
                wishlist.get().getBooks().remove(book.get());
                wishlistRepository.save(wishlist.get());
            } else {
                throw new RuntimeException("Book not found with id: " + bookId);
            }
        }else{
            throw new RuntimeException("Wishlist not found for user with id: " + userId);
        }
    }

    @Override
    public Wishlist getWishlistByUserId(Long userId) {
          Optional<Wishlist> wishlist = wishlistRepository.findByUserId(userId);
        return wishlist.orElseGet(Wishlist::new);
    }

    @Override
    public Optional<Wishlist> getWishlistById(Long id) {
        return wishlistRepository.findById(id);
    }

    @Override
    public Wishlist updateWishlist(Long id, Wishlist updatedWishlist) {
        if (wishlistRepository.existsById(id)) {
            updatedWishlist.setId(id);
            return wishlistRepository.save(updatedWishlist);
        }
        return null; // or throw an exception
    }

    @Override
    public void deleteWishlist(Long id) {
        wishlistRepository.deleteById(id);
    }
}
