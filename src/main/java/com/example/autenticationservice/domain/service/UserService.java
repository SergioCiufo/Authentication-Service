package com.example.autenticationservice.domain.service;

import com.example.autenticationservice.domain.exceptions.InvalidCredentialsException;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.util.UserListUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserListUtil userListUtil;

    public User validateCredentials(String username, String password) {
        return userListUtil.getUserList().stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .orElseThrow(() -> new InvalidCredentialsException("Username o Password errati"));
    }
}