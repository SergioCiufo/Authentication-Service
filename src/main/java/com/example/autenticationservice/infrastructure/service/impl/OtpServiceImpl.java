package com.example.autenticationservice.infrastructure.service.impl;

import com.example.autenticationservice.domain.api.OtpServiceApi;
import com.example.autenticationservice.domain.exceptions.InvalidCredentialsException;
import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.infrastructure.api.OtpUtil;
import com.example.autenticationservice.infrastructure.service.OtpRepository;
import com.example.autenticationservice.infrastructure.service.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpServiceApi {
    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final OtpUtil otpUtil;

    @Override
    public void updateOtp(Otp otp) {
        otpRepository.save(otp);
    }

    @Override
    public Optional<Otp> getValidOtpBySessionId(String sessionId) {
        return otpRepository.findOtpBySessionIdAndValidTrue(sessionId);
    }

    @Override
    @Transactional
    public Otp getNewOtp(String sessionId, String username) {
        Optional<Otp> oldOtp = otpRepository.findOtpBySessionId(sessionId);
        if(oldOtp.isEmpty()) {
            //throw oldOtp Assente
        }

        otpRepository.invalidateOtp(oldOtp.get().getOtp());
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isEmpty()){
            throw new InvalidCredentialsException("username not found"); //todo da cambiare il tipo di eccezione?
        }

        Otp newOtp = otpUtil.generateOtp(user.get(), sessionId); //risale al domain

        otpRepository.save(newOtp);

        return newOtp;
    }
}
