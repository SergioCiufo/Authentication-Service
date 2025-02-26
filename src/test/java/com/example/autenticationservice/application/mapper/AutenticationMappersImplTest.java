package com.example.autenticationservice.application.mapper;

import com.example.autenticationservice.domain.model.autentication.FirstStepLoginRequest;
import com.example.autenticationservice.domain.model.register.StepRegisterRequest;
import com.example.autenticationservice.domain.model.register.StepRegisterResponse;
import com.example.autenticationservice.generated.application.model.Register200Response;
import com.example.autenticationservice.generated.application.model.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AutenticationMappersImplTest {

    private final AutenticationMappersImpl autenticationMappersImpl = new AutenticationMappersImpl();

    @Test
    void shouldConvertToDomainRegisterRequest_whenAllOk(){
        //PARAMETERS
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("testName");
        registerRequest.setUsername("testUsername");
        registerRequest.setEmail("testEmail");
        registerRequest.setPassword("testPassword");

        //TEST
        StepRegisterRequest result = autenticationMappersImpl.convertToDomain(registerRequest);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(registerRequest.getName(),result.getName());
        Assertions.assertEquals(registerRequest.getUsername(), result.getUsername());
        Assertions.assertEquals(registerRequest.getEmail(), result.getEmail());
        Assertions.assertEquals(registerRequest.getPassword(), result.getPassword());
    }

    @Test
    void shouldReturnNullRegisterRequest_whenRequestIsNull(){
        //PARAMETERS
        RegisterRequest registerRequest = null;

        //TEST
        StepRegisterRequest result = autenticationMappersImpl.convertToDomain(registerRequest);

        //RESULTS
        Assertions.assertNull(result);
    }

    @Test
    void shouldConvertFromDomainRegister200Response_whenAllOk(){
        //PARAMETERS
        StepRegisterResponse stepRegisterResponse = new StepRegisterResponse();
        stepRegisterResponse.setMessage("Success");

        //TEST
        Register200Response result = autenticationMappersImpl.convertFromDomain(stepRegisterResponse);

        //RESULTS
        Assertions.assertNotNull(result);
    }

    @Test
    void shouldReturnNullRegister200Response_whenResponseIsNull(){
        //PARAMETERS
        StepRegisterResponse stepRegisterResponse = null;

        //TEST
        Register200Response result = autenticationMappersImpl.convertFromDomain(stepRegisterResponse);
        //RESULTS
        Assertions.assertNull(result);
    }

    @Test
    void shouldConvertToDomainFirstStepRegisterRequest_whenAllOk(){
        //PARAMETERS
        FirstStepLoginRequest firstStepLoginRequest = new FirstStepLoginRequest();

        //TEST

        //RESULTS
    }
}
