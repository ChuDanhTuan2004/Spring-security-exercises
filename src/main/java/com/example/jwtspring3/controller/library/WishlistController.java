package com.example.jwtspring3.controller.library;

import com.example.jwtspring3.model.User;
import com.example.jwtspring3.model.library.Wishlist;
import com.example.jwtspring3.service.library.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/wishlist")
@CrossOrigin
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/{id}")
    public ResponseEntity<Wishlist> getWishlistById(@PathVariable Long id) {

        Wishlist wishlist = wishlistService.getWishlistByUserId(id);
        if (wishlist != null) {
            return new ResponseEntity<>(wishlist, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Wishlist> updateWishlist(@PathVariable Long id, @RequestBody User user) {
        Wishlist updatedWishlist = wishlistService.addBookToWishlist(id, user.getId());
        if (updatedWishlist != null) {
            return new ResponseEntity<>(updatedWishlist, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteWishlist(@PathVariable Long id, @RequestBody Wishlist wishlist) {
        wishlistService.removeBookFromWishlist(id,wishlist.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
