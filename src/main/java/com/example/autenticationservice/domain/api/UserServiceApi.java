package com.example.autenticationservice.domain.api;

import com.example.autenticationservice.domain.model.User;

import java.util.Optional;

public interface UserServiceApi {
    Optional<User> getUserByUsername(String username);
    void register(User user);
}
