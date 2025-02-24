package com.example.autenticationservice.domain.repository;

import com.example.autenticationservice.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserServiceRepo {
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByUsernameAndPassword(String username, String password);
    void register(User user);
    List<User> getUserList();
}
