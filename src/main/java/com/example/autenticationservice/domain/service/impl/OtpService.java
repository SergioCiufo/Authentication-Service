package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.api.OtpServiceApi;
import com.example.autenticationservice.domain.api.UserServiceApi;
import com.example.autenticationservice.domain.exceptions.CredentialTakenException;
import com.example.autenticationservice.domain.exceptions.InvalidSessionException;
import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.util.OtpUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
//@Transactional //fa sì che ci sia un'unica transazione anche usando più repository // non si dovrebbe usare qui??
@AllArgsConstructor
@Log4j2
public class OtpService {
    private final OtpServiceApi otpServiceApi;
    private final UserServiceApi userServiceApi;
    private final OtpUtil otpUtil;

    public void addOtp(Otp otp) {
        otpServiceApi.addOtp(otp);
    }

    public Otp validateUserAndGenerateOtp(String username, String password, String sessionId) {
        Optional<User> user = userServiceApi.getUserByUsernameAndPassword(username, password);

        if (user.isEmpty()) {
            throw new CredentialTakenException("Invalid credentials");
        }

        Otp otp = otpUtil.generateOtp(user.get(), sessionId);

        otpServiceApi.addOtp(otp);
        return otp;
    }

    public Otp getOtpBySessionId(String sessionId) {
        Optional<Otp> otp = otpServiceApi.getValidOtpBySessionId(sessionId);
        if(otp.isEmpty()) {
            throw new InvalidSessionException("Invalid session");
        }
        return otp.get();
    }

    public void updateOtp(Otp otp) {
        otpServiceApi.updateOtp(otp);
    }

    public Otp getNewOtp(String sessionId, String username){
        return otpServiceApi.getNewOtp(sessionId, username);
    }

}
