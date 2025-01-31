package com.example.MovieApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MovieApp.request.UserLoginRequest;
import com.example.MovieApp.request.UserRegisterRequest;
import com.example.MovieApp.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Регистрация с ролью USER
    @PostMapping("/register/user")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegisterRequest user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Username and password are required!");
        }

        userService.registerAsUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    // Регистрация с ролью ADMIN
    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody UserRegisterRequest user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Username and password are required!");
        }

        userService.registerAsAdmin(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Admin registered successfully!");
    }

    // Логин пользователя
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginRequest user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Username and password are required!");
        }
        return userService.login(user);
    }
}
