package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.api.OtpServiceRepo;
import com.example.autenticationservice.domain.exceptions.InvalidSessionException;
import com.example.autenticationservice.domain.model.entities.Otp;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class OtpService {
    private final OtpServiceRepo otpServiceRepo;

    public Otp getOtpBySessionId(String sessionId) {
        Optional<Otp> otp = otpServiceRepo.getValidOtpBySessionId(sessionId);
        if(otp.isEmpty()) {
            throw new InvalidSessionException("Invalid session");
        }
        return otp.get();
    }

    public void saveOtp(Otp otp) { otpServiceRepo.saveOtp(otp);}

    public void updateOtp(Otp otp) {
        otpServiceRepo.updateOtp(otp);
    }

    public void invalidateOtp(Otp otp) { otpServiceRepo.invalidateOtp(otp);}
}
