package com.example.autenticationservice.application.mapper;

import com.example.autenticationservice.domain.model.verifyToken.FirstStepVerifyTokenResponse;
import com.example.autenticationservice.generated.application.model.VerifyToken200Response;
import org.mapstruct.Mapper;

@Mapper
public interface VerifyTokenMappers {
    VerifyToken200Response convertFromDomain(FirstStepVerifyTokenResponse response);
}
