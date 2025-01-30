package com.example.autenticationservice.domain.service;

import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.util.OtpListUtil;
import com.example.autenticationservice.domain.util.OtpUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class OtpService {
    private final OtpListUtil otpListUtil;
    private final OtpUtil otpUtil;

    public Otp getOtpByUserOtp(String userOtp){
        List<Otp> otpList = otpListUtil.getOtpList();

        return otpList.stream()
                .filter(otp -> otp.getOtp().equals(userOtp) && otp.isValid()) //se esiste ed è valido
                .findFirst() // Prende il primo trovato
                .orElse(null); //sennò da null
    }

    public Otp generateOtp(User user, String sessionId){
        String otpGenerated = otpUtil.generateOtp();
        long creationDate = Instant.now().toEpochMilli();
        long expirationDate = otpUtil.calculateOtpExpirationTime();
        Otp otp = new Otp(null, user, otpGenerated, sessionId,creationDate, expirationDate, 0, true);
        return otp;
    }

    public void add(Otp otp) {
        otpListUtil.getOtpList().add(otp);
    }

    public Otp getOtpBySessionId(String sessionId) {
        List<Otp> otpList = otpListUtil.getOtpList();

        return otpList.stream()
                .filter(otp -> otp.getSessionId().equals(sessionId) && otp.isValid())
                .findFirst()
                .orElse(null);
    }

    public void setOtpInvalid(Otp otp) {
        if (otp != null) {
            otp.setValid(false);
        }
    }

    public void updateAttempt(Otp otp, Integer attempt) {
        if (otp != null) {
            otp.setAttempts(attempt);
        }
    }
}
