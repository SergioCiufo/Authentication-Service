package com.example.autenticationservice.infrastructure.service.impl;

import com.example.autenticationservice.domain.api.OtpServiceRepo;
import com.example.autenticationservice.domain.model.entities.Otp;
import com.example.autenticationservice.infrastructure.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service //sempre un servizio
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpServiceRepo {
    private final OtpRepository otpRepository;

    @Override
    public void saveOtp(Otp otp) {
        otpRepository.save(otp);
    }

    @Override
    public void updateOtp(Otp otp) {
        otpRepository.save(otp);
    }

    @Override
    public Optional<Otp> getValidOtpBySessionId(String sessionId) {
        return otpRepository.findOtpBySessionIdAndValidTrue(sessionId);
    }

    @Override
    public void invalidateOtp(Otp otp) {
        otpRepository.invalidateOtp(otp.getOtp());
    }
}
