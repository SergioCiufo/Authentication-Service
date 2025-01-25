package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.exceptions.CredentialTakenException;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.model.register.FirstStepRegisterRequest;
import com.example.autenticationservice.domain.model.register.FirstStepRegisterResponse;
import com.example.autenticationservice.domain.service.RegisterService;
import com.example.autenticationservice.domain.util.UserListUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RegisterServiceImpl  implements RegisterService {

    private final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);

    private final UserListUtil userListUtil;

    @Override
    public FirstStepRegisterResponse firstStep(FirstStepRegisterRequest firstStepRegisterRequest) {
        String name = firstStepRegisterRequest.getName();
        String username = firstStepRegisterRequest.getUsername();
        String email = firstStepRegisterRequest.getEmail();
        String password = firstStepRegisterRequest.getPassword();

        User newUser = new User(null, name, username, email, password, null);

        String registerValid = registerValid(newUser);
        if(registerValid != null) {
            logger.error(registerValid);
            throw new CredentialTakenException(registerValid);
        }
        userListUtil.add(newUser);

        return FirstStepRegisterResponse.builder()
                .message("Registrazione effettuata")
                .build();
    }

    private String registerValid(User newUser) {
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
