package com.example.usercrud.controller;

import com.example.usercrud.model.User;
import com.example.usercrud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        User user = userRepository.findByEmailAndPassword(email, password);
        return user != null ? "{\"status\": \"success\"}" : "{\"status\": \"fail\"}";
    }

    @PostMapping("/create")
    public String create(@RequestBody User user) {
        userRepository.save(user);
        return "{\"status\": \"user_created\"}";
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable Long id, @RequestBody User user) {
        User existing = userRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setName(user.getName());
            existing.setEmail(user.getEmail());
            userRepository.save(existing);
            return "{\"status\": \"user_updated\"}";
        }
        return "{\"status\": \"user_not_found\"}";
    }

    @PatchMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setActive(!user.isActive());
            userRepository.save(user);
            return "{\"status\": \"user_toggled\"}";
        }
        return "{\"status\": \"user_not_found\"}";
    }

    @GetMapping
    public List<User> listAll() {
        return userRepository.findAll();
    }
}