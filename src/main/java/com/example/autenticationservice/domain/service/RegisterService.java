package com.example.autenticationservice.domain.service;

import com.example.autenticationservice.domain.exceptions.CredentialTakenException;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.util.UserListUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class RegisterService {

    private final UserListUtil userListUtil;
    private final UserService userService;

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

    public void registerUser(User user) {
        String valid = registerValid(user);
        if(valid != null) {
            log.error(valid);
            throw new CredentialTakenException(valid);
        }
        userService.add(user);
    }
}
