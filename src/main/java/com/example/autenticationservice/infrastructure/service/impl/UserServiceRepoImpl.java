package com.example.autenticationservice.infrastructure.service.impl;

import com.example.autenticationservice.domain.api.UserServiceRepo;
import com.example.autenticationservice.domain.exceptions.CredentialTakenException;
import com.example.autenticationservice.domain.exceptions.InvalidCredentialsException;
import com.example.autenticationservice.domain.model.entities.User;
import com.example.autenticationservice.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserServiceRepoImpl implements UserServiceRepo {
    private final UserRepository userRepository;
//userEntity per staccare?
    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getUserByUsernameAndPassword(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if(user.isEmpty()){
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return user;
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
