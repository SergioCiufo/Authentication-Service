package com.example.autenticationservice.infrastructure.service.impl;

import com.example.autenticationservice.domain.api.OtpServiceRepo;
import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.infrastructure.mapper.EntityMappers;
import com.example.autenticationservice.infrastructure.model.OtpEntity;
import com.example.autenticationservice.infrastructure.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service //sempre un servizio
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpServiceRepo {
    private final OtpRepository otpRepository;
    private final EntityMappers entityMappers;

    @Override
    public void saveOtp(Otp otp) {
        OtpEntity otpEntity = entityMappers.convertFromDomain(otp);
        otpRepository.save(otpEntity);
    }

    @Override
    public void updateOtp(Otp otp) {
        OtpEntity otpEntity = entityMappers.convertFromDomain(otp);
        otpRepository.save(otpEntity);
    }

    @Override
    public Optional<Otp> getValidOtpBySessionId(String sessionId) {
        return otpRepository.findOtpBySessionIdAndValidTrue(sessionId)
                .map(entityMappers::convertToDomain);
    }

    @Override
    public void invalidateOtp(Otp otp) {
        otpRepository.invalidateOtp(otp.getOtp());
    }
}
