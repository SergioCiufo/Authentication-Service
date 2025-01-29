package com.example.autenticationservice.domain.service;

import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.util.UserListUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterService {

    private final UserListUtil userListUtil;

    public String registerValid(User newUser) {
        boolean usernameExist = userListUtil.getUserList().stream()
                .anyMatch(user -> user.getUsername().equals(newUser.getUsername()));

        boolean emailExist = userListUtil.getUserList().stream()
                .anyMatch(user -> user.getEmail().equals(newUser.getEmail()));

        if (usernameExist) {
            return "Username già in uso";
        } else if (emailExist) {
            return "Email già in uso";
        }

        return null;
    }
}
