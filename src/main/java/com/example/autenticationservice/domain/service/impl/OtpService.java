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
@AllArgsConstructor
@Log4j2
public class OtpService {
    private final OtpServiceApi otpServiceApi;

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
