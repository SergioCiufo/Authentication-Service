package com.example.autenticationservice.application.mapper;

import com.example.autenticationservice.domain.model.verifyOtp.FirstStepVerifyOtpRequest;
import com.example.autenticationservice.domain.model.verifyOtp.FirstStepVerifyOtpResponse;
import com.example.autenticationservice.generated.application.model.VerifyOTP200Response;
import com.example.autenticationservice.generated.application.model.VerifyOTPRequest;
import org.mapstruct.Mapper;

@Mapper
public interface VerifyOtpMappers {
    FirstStepVerifyOtpRequest convertToDomain(VerifyOTPRequest verifyOTPRequest);
    VerifyOTP200Response convertFromDomain(FirstStepVerifyOtpResponse firstStepVerifyOtpResponse);
}
