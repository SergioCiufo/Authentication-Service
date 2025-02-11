package com.example.autenticationservice.infrastructure.service.impl;

import com.example.autenticationservice.domain.api.UserServiceApi;
import com.example.autenticationservice.domain.exceptions.CredentialTakenException;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.infrastructure.service.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserServiceImpl implements UserServiceApi {
    private final UserRepository userRepository;
//userEntity per staccare?
    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public void register(User user) {
        Optional<User> existingUserByUsername = userRepository.findByUsername(user.getUsername());
        Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());

        if (existingUserByUsername.isPresent() && existingUserByEmail.isPresent()) {
            throw new CredentialTakenException("Username and email already exists");
        }

        if (existingUserByUsername.isPresent()) {
            throw new CredentialTakenException("Username is already taken");
        }

        if (existingUserByEmail.isPresent()) {
            throw new CredentialTakenException("Email is already taken");
        }

        userRepository.save(user);

    }

}
