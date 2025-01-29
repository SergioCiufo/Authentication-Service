package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.exceptions.InvalidCredentialsException;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.model.login.FirstStepRequest;
import com.example.autenticationservice.domain.model.login.FirstStepResponse;
import com.example.autenticationservice.domain.service.EmailService;
import com.example.autenticationservice.domain.service.LoginService;
import com.example.autenticationservice.domain.util.OtpUtil;
import com.example.autenticationservice.domain.util.UserListUtil;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {
/*
    private List<Map.Entry<String, String>> userCredentialsList = Arrays.asList(
            new AbstractMap.SimpleEntry<>("admin", "admin"),
            new AbstractMap.SimpleEntry<>("username", "password"),
            new AbstractMap.SimpleEntry<>("test", "test")
    );
*/
/*
    private List<User> userList = new ArrayList<>() {{
        add(new User("admin", "admin", "admin@admin.com", "admin"));
        add(new User("name", "username", "email@email.com", "password"));
        add(new User("test", "test", "test@test.com", "test"));
}};
*/
    private final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    private final UserListUtil userListUtil;
    private final OtpUtil otpUtil;
    private final EmailService emailService;
    //private final PasswordEncryptionUtil passwordEncryptionUtil;

    @Override
    public FirstStepResponse firstStep(FirstStepRequest firstStepRequest, HttpSession session) { //ho aggiunto la session per il sessionId

        String username = firstStepRequest.getUsername();
        String password = firstStepRequest.getPassword();
        String sessionId = session.getId();

        if(!credentialValid(username, password)) {
            logger.error("Username o Password errati");
            throw new InvalidCredentialsException("Username o Password errati");
        }

        //settiamo l'opt nella sessione
        String otp = otpUtil.generateOtp();

        //usiamo optional per prenderci l'utente per poi pescare la mail
        Optional<User> user = userListUtil.getUserList().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();

        String emailReceiver = user.get().getEmail();
        session.setAttribute("username", user.get().getUsername());

        session.setAttribute("user", user);

        String emailSubject = "Chat4Me - OTP code";
        //invia la mail con l'otp
        emailService.sendEmail(emailReceiver, emailSubject, otp);

        session.setAttribute("otp", otp);
        logger.info(String.format("OTP: %s", session.getAttribute("otp")));
        //System.out.println("OTP Salvato in sessione: " + session.getAttribute("otp"));

        //settiamo l'orario in cui è stato registrato l'otp (in millisecondi)
        long otpExpireTime = System.currentTimeMillis() + 1*60*1000; //durata 1 minuti
        session.setAttribute("otpExpireTime", otpExpireTime);

        logger.info("OTP Expire Time: {}",
                Instant.ofEpochMilli((Long) session.getAttribute("otpExpireTime"))
                        .atZone(ZoneId.systemDefault())
                        .toLocalTime()
                        .format(DateTimeFormatter.ofPattern("HH:mm")));

//        //settiamo il sessionId (lo usiamo per il secondo check)
//        session.setAttribute("sessionId", sessionId);
//        logger.info(String.format("Session ID: %s", sessionId));

        //set in maniera più fluida
        return FirstStepResponse.builder()
                .message("Login effettuato, OTP inviato")
                .sessionId(sessionId)
                .build();
    }

    private boolean credentialValid(String username, String password) {
        return userListUtil.getUserList().stream()
                .filter(user -> user.getUsername().equals(username))
                .anyMatch(user -> user.getPassword().equals(password));
    }

    //con password criptata
    /*
    private boolean credentialValid(String username, String password) {
        return userListUtil.getUserList().stream()
                .filter(user -> user.getUsername().equals(username))
                .anyMatch(user -> passwordEncryptionUtil.verifyPassword(password, user.getPassword()));
    }

     */
}
