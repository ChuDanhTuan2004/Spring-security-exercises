package com.example.jwtspring3.controller;

import com.example.jwtspring3.model.JwtResponse;
import com.example.jwtspring3.model.Role;
import com.example.jwtspring3.model.User;
import com.example.jwtspring3.request.UserDTO;
import com.example.jwtspring3.service.RoleService;
import com.example.jwtspring3.service.UserService;
import com.example.jwtspring3.service.impl.JwtService;
import com.example.jwtspring3.service.library.impl.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin("*")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;


    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> showAllUser() {
        Iterable<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/admin/users")
    public ResponseEntity<Iterable<User>> showAllUserByAdmin() {
        Iterable<User> users = userService.findAll();
        List<User> nonAdminUsers = new ArrayList<>();
        for (User user : users) {
            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
            if (!isAdmin) {
                nonAdminUsers.add(user);
            }
        }
        return new ResponseEntity<>(nonAdminUsers, HttpStatus.OK);
    }
    @GetMapping("/users/append")
    public ResponseEntity<List<User>> showAllAppendAccount(){
        return new ResponseEntity<>(userService.findUsersAppend(),HttpStatus.OK);
    }
    @PutMapping("/users/append/{id}")
    public ResponseEntity<?> updateAppendAccount(@PathVariable Long id){
        userService.acceptUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Iterable<User> users = userService.findAll();
        for (User currentUser : users) {
            if (currentUser.getUsername().equals(user.getUsername())) {
                return new ResponseEntity<>("Username existed", HttpStatus.OK);
            }
        }
        if (!userService.isCorrectConfirmPassword(user)) {
            return new ResponseEntity<>("Input confirm password", HttpStatus.OK);
        }
        if (user.getRoles() != null) {
            Role role = roleService.findByName("ROLE_ADMIN");
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
        } else {
            Role role1 = roleService.findByName("ROLE_USER");
            Set<Role> roles1 = new HashSet<>();
            roles1.add(role1);
            user.setRoles(roles1);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        user.setEnabled(false);
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateTokenLogin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.findByUsername(user.getUsername());
        return ResponseEntity.ok(new JwtResponse(jwt, currentUser.getId(), userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity("Hello World", HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getProfile(@PathVariable Long id) {
        Optional<User> userOptional = this.userService.findById(id);
        return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            // Thêm token vào blacklist (cần triển khai)
            jwtService.addToBlacklist(jwt);
        }

        // Xóa authentication khỏi SecurityContext
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Logged out successfully");
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User existingUser = userOptional.get();
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setRoles(user.getRoles());
        userService.save(existingUser);
        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<User>> searchUsersByName(@RequestParam String name) {
        List<User> users = userService.searchByName(name);
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/current")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDTO userDTO = userService.getUserByUsername(username);
        return ResponseEntity.ok(userDTO);
    }
}
