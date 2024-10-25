package com.example.jwtspring3.controller.library;

import com.example.jwtspring3.model.User;
import com.example.jwtspring3.model.library.Wishlist;
import com.example.jwtspring3.service.library.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
@CrossOrigin("*")
public class WishlistController {

    private static final Logger logger = LoggerFactory.getLogger(WishlistController.class);

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/{id}")
    public ResponseEntity<Wishlist> getWishlistById(@PathVariable Long id) {
        try {
            Wishlist wishlist = wishlistService.getWishlistByUserId(id);
            if (wishlist != null) {
                return new ResponseEntity<>(wishlist, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error in getWishlistById: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateWishlist(@PathVariable Long userId, @RequestBody JsonNode requestBody) {
        try {
            logger.info("Received request body: {}", requestBody.toString());

            Wishlist updatedWishlist = null;
            if (requestBody.isArray()) {
                for (JsonNode node : requestBody) {
                    if (node.has("id")) {
                        Long bookId = node.get("id").asLong();
                        updatedWishlist = wishlistService.addBookToWishlist(userId, bookId);
                        logger.info("Added book with ID {} to wishlist", bookId);
                    }
                }
            } else {
                logger.warn("Invalid request body format");
                return new ResponseEntity<>("Invalid request body format", HttpStatus.BAD_REQUEST);
            }

            if (updatedWishlist == null) {
                logger.warn("No books were added to the wishlist");
                return new ResponseEntity<>("No books were added to the wishlist", HttpStatus.BAD_REQUEST);
            }

            logger.info("Updated wishlist: {}", updatedWishlist); // Add this line
            return new ResponseEntity<>(updatedWishlist, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in updateWishlist: ", e);
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteWishlist(@PathVariable Long id, @RequestBody Wishlist wishlist) {
        try {
            wishlistService.removeBookFromWishlist(id, wishlist.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error in deleteWishlist: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}