package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.repository.UserServiceRepo;
import com.example.autenticationservice.domain.exceptions.InvalidCredentialsException;
import com.example.autenticationservice.domain.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserServiceRepo userServiceRepo;

    public void register(User user) { //?!
        userServiceRepo.register(user);
    }

    public User getUserByUsername(String username) {
        return userServiceRepo.getUserByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return userServiceRepo.getUserByUsernameAndPassword(username, password)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
    }

    public List<User> getUserList(){
        return userServiceRepo.getUserList();
    }
}