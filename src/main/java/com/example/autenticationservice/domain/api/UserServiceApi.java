package com.example.autenticationservice.domain.api;

import com.example.autenticationservice.domain.model.User;

import java.util.Optional;

public interface UserServiceApi {
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByUsernameAndPassword(String username, String password);
    void addUser(User user);
}
