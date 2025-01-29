package com.example.autenticationservice.application.mapper;

import com.example.autenticationservice.domain.model.login.FirstStepRequest;
import com.example.autenticationservice.domain.model.login.FirstStepResponse;
import com.example.autenticationservice.generated.application.model.Login200Response;
import com.example.autenticationservice.generated.application.model.LoginRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface LoginMappers {

    //@Mapping(target = "username", source = "request.username") nel caso dovessero avere nomi diversi o rompere delle incosistenze
    FirstStepRequest convertToDomain(LoginRequest request);
    @Mapping(target = "sessionOtp", source = "response.sessionId")
    Login200Response convertFromDomain(FirstStepResponse response);
}
