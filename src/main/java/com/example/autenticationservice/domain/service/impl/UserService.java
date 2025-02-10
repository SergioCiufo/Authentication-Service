package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.api.UserServiceApi;
import com.example.autenticationservice.domain.exceptions.CredentialTakenException;
import com.example.autenticationservice.domain.exceptions.InvalidCredentialsException;
import com.example.autenticationservice.domain.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserServiceApi userServiceApi;

    public void register(User user) { //?!
        userServiceApi.register(user);
    }

    public User getUserByUsername(String username) {
        Optional <User> user = userServiceApi.getUserByUsername(username);
        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return user.get();
    }
}