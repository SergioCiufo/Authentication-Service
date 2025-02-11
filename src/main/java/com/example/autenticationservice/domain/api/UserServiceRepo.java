package com.example.autenticationservice.domain.api;

import com.example.autenticationservice.domain.model.entities.User;

import java.util.Optional;

public interface UserServiceRepo {
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByUsernameAndPassword(String username, String password);
    void register(User user);
}
