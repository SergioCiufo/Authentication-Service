package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.api.UserServiceRepo;
import com.example.autenticationservice.domain.exceptions.InvalidCredentialsException;
import com.example.autenticationservice.domain.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserServiceRepo userServiceRepo;

    public void register(User user) { //?!
        userServiceRepo.register(user);
    }

    public User getUserByUsername(String username) {
        Optional <User> user = userServiceRepo.getUserByUsername(username);
        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return user.get();
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        Optional<User> user = userServiceRepo.getUserByUsernameAndPassword(username, password);
        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return user.get();
    }
}