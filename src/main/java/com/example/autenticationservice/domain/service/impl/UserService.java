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
        Optional<User> existingUserByUsername  = userServiceApi.getUserByUsername(user.getUsername());
        Optional<User> existingUserByEmail = userServiceApi.getUserByEmail(user.getEmail());

        if (existingUserByUsername.isPresent() && existingUserByEmail.isPresent()) {
            throw new CredentialTakenException("Username and email already exists");
        }

        if (existingUserByUsername.isPresent()) {
            throw new CredentialTakenException("Username is already taken");
        }

        if (existingUserByEmail.isPresent()) {
            throw new CredentialTakenException("Email is already taken");
        }

        userServiceApi.addUser(user);
    }

    public User getUserByUsername(String username) {
        Optional <User> user = userServiceApi.getUserByUsername(username);
        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return user.get();
    }

}