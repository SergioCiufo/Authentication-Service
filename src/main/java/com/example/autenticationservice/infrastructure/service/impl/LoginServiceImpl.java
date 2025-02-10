package com.example.autenticationservice.infrastructure.service.impl;

import com.example.autenticationservice.domain.api.LoginServiceApi;
import com.example.autenticationservice.domain.exceptions.InvalidCredentialsException;
import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.infrastructure.api.OtpUtil;
import com.example.autenticationservice.infrastructure.service.OtpRepository;
import com.example.autenticationservice.infrastructure.service.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginServiceApi {
    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final OtpUtil otpUtil;

    @Override
    @Transactional
    public Otp validateUserAndGenerateOtp(String username, String password, String sessionId) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if(user.isEmpty()){
            throw new InvalidCredentialsException("Invalid credentials");
        }

        Otp otp = otpUtil.generateOtp(user.get(), sessionId);

        otpRepository.save(otp);
        return otp;
    }
}
