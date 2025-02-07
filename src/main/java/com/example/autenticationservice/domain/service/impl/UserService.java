package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.exceptions.InvalidCredentialsException;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.util.UserListUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserListUtil userListUtil;

    public User validateCredentials(String username, String password) {
        return userListUtil.getUserList().stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .orElseThrow(() -> new InvalidCredentialsException("Incorrect username or password"));
    }

    public void add(User user) {
        userListUtil.getUserList().add(user);
    }

    public void updateUserOtpList(User user){
        List<User> userList = userListUtil.getUserList();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUsername().equals(user.getUsername())) {
                User existingUser = userList.get(i); // Trova l'utente
                existingUser.setOtpList(user.getOtpList()); // Aggiorna la lista OTP
                return;
            }
        }
    }

    public User getUserFromUsername(String username) {
       return userListUtil.getUserList().stream()
               .filter(user -> user.getUsername().equals(username))
               .findFirst()
               .orElse(null);
    }

}