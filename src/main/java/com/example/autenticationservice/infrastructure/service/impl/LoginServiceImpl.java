package com.example.autenticationservice.infrastructure.service.impl;

import com.example.autenticationservice.domain.api.LoginServiceApi;
import com.example.autenticationservice.domain.exceptions.ExpireOtpException;
import com.example.autenticationservice.domain.exceptions.InvalidCredentialsException;
import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.infrastructure.api.OtpUtil;
import com.example.autenticationservice.infrastructure.api.RefreshTokenJwtInf;
import com.example.autenticationservice.infrastructure.service.OtpRepository;
import com.example.autenticationservice.infrastructure.service.RefreshTokenRepository;
import com.example.autenticationservice.infrastructure.service.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginServiceApi {
    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OtpUtil otpUtil;
    private final RefreshTokenJwtInf refreshTokenJwtInf;

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

    @Override
    @Transactional
    public RefreshToken validateOtpAndGenerateToken(String username, String sessionId, String requestOtp) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new InvalidCredentialsException("Invalid credentials"); //da vedere
        }

        Optional<Otp> dbOtp = otpRepository.findOtpBySessionId(sessionId);
        if(dbOtp.isEmpty()){
            throw new InvalidCredentialsException("Invalid credentials"); //da vedere
        }

        if(otpUtil.checkOtpMaxAttempt(dbOtp.get())){
            otpRepository.delete(dbOtp.get());
            throw new ExpireOtpException("OTP entry attemps exhausted");
        }

        //controlli validità otp
        if(!requestOtp.equals(dbOtp.get().getOtp())){
            otpUtil.increaseOtpAttempt(dbOtp.get());
            otpRepository.save(dbOtp.get());
            throw new InvalidCredentialsException("OTP not valid");
        }

        if(otpUtil.isOtpExpired(dbOtp.get())){
            otpRepository.invalidateOtp(dbOtp.get().getOtp());
            throw new ExpireOtpException("OTP expired");
        }



        RefreshToken refreshToken = refreshTokenJwtInf.refreshToken(user.get());

        refreshTokenRepository.save(refreshToken);

        otpRepository.invalidateOtp(dbOtp.get().getOtp());

        return refreshToken;
    }
}
