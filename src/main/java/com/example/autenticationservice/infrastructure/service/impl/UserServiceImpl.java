package com.example.autenticationservice.infrastructure.service.impl;

import com.example.autenticationservice.domain.api.UserServiceApi;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.infrastructure.service.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserServiceApi {
    private final UserRepository userRepository;
//userEntity per staccare?
    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }
}
