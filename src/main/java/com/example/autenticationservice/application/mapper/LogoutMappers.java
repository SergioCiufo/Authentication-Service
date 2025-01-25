package com.example.autenticationservice.application.mapper;

import com.example.autenticationservice.domain.model.logout.FirstStepLogoutResponse;
import com.example.autenticationservice.generated.application.model.Logout200Response;
import org.mapstruct.Mapper;

@Mapper
public interface LogoutMappers {

    Logout200Response convertFromDomain(FirstStepLogoutResponse response);
}
