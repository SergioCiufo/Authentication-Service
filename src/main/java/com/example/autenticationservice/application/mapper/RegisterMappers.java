package com.example.autenticationservice.application.mapper;

import com.example.autenticationservice.domain.model.register.FirstStepRegisterRequest;
import com.example.autenticationservice.domain.model.register.FirstStepRegisterResponse;
import com.example.autenticationservice.generated.application.model.Register200Response;
import com.example.autenticationservice.generated.application.model.RegisterRequest;
import org.mapstruct.Mapper;

@Mapper
public interface RegisterMappers {
    FirstStepRegisterRequest convertToDomain(RegisterRequest request);
    Register200Response convertFromDomain(FirstStepRegisterResponse response);
}
