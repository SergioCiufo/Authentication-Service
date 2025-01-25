package com.example.autenticationservice.application.mapper;

import com.example.autenticationservice.domain.model.ResendOtp.FirstStepResendOtpRequest;
import com.example.autenticationservice.domain.model.ResendOtp.FirstStepResendOtpResponse;
import com.example.autenticationservice.generated.application.model.ReSendOtp200Response;
import com.example.autenticationservice.generated.application.model.ReSendOtpRequest;
import org.mapstruct.Mapper;

@Mapper
public interface ResendOtpMappers {
    FirstStepResendOtpRequest convertToDomain(ReSendOtpRequest request);
    ReSendOtp200Response convertFromDomain(FirstStepResendOtpResponse resendOtpResponse);
}
