package com.example.autenticationservice.application;

import com.example.autenticationservice.application.mapper.AutenticationMappers;
import com.example.autenticationservice.application.service.JwtServiceImpl;
import com.example.autenticationservice.domain.model.autentication.FirstStepLoginRequest;
import com.example.autenticationservice.domain.model.autentication.FirstStepLoginResponse;
import com.example.autenticationservice.domain.model.autentication.SecondStepLoginRequest;
import com.example.autenticationservice.domain.model.autentication.SecondStepLoginResponse;
import com.example.autenticationservice.domain.model.register.StepRegisterRequest;
import com.example.autenticationservice.domain.model.register.StepRegisterResponse;
import com.example.autenticationservice.domain.service.AutenticationService;
import com.example.autenticationservice.generated.application.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServizioAutenticazioneApiDelegateImplTest {

    @InjectMocks
    private ServizioAutenticazioneApiDelegateImpl servizioAutenticazioneApiDelegateImpl;

    @Mock
    private AutenticationMappers  autenticationMappers;

    @Mock
    private AutenticationService autenticationService;

    @Mock
    private JwtServiceImpl  jwtServiceImpl;

    @Test
    void shouldRegister_whenAllOk(){
        //PARAMETERS
        RegisterRequest registerRequest = new RegisterRequest();
        StepRegisterRequest request = new StepRegisterRequest();
        StepRegisterResponse response = new StepRegisterResponse();
        Register200Response convertedResponse = new Register200Response();

        //MOCK
        doReturn(request).when(autenticationMappers).convertToDomain(registerRequest);
        doReturn(response).when(autenticationService).register(request);
        doReturn(convertedResponse).when(autenticationMappers).convertFromDomain(response);

        //TEST
        ResponseEntity<Register200Response> result = servizioAutenticazioneApiDelegateImpl.register(registerRequest);

        //RESULTS
        Assertions.assertEquals(convertedResponse, result.getBody());
    }

    @Test
    void shouldThrowException_whenRegisterFail(){
        //PARAMETERS
        RegisterRequest registerRequest = new RegisterRequest();

        //MOCK
        doThrow(RuntimeException.class).when(autenticationMappers).convertToDomain(registerRequest);

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () -> {
            servizioAutenticazioneApiDelegateImpl.register(registerRequest);
        });
    }

    @Test
    void shouldLogin_whenAllOk(){
        //PARAMETERS
        LoginRequest loginRequest = new LoginRequest();
        FirstStepLoginRequest request = new FirstStepLoginRequest();
        FirstStepLoginResponse response = new FirstStepLoginResponse();
        Login200Response convertedResponse = new Login200Response();

        //MOCK
        doReturn(request).when(autenticationMappers).convertToDomain(loginRequest);
        doReturn(response).when(autenticationService).firstStepLogin(request);
        doReturn(convertedResponse).when(autenticationMappers).convertFromDomain(response);

        //TEST
        ResponseEntity<Login200Response> result = servizioAutenticazioneApiDelegateImpl.login(loginRequest);

        //RESULTS
        Assertions.assertEquals(convertedResponse, result.getBody());
    }

    @Test
    void shouldThrowException_whenLoginFail(){
        //PARAMETERS
        LoginRequest loginRequest = new LoginRequest();

        //MOCK
        doThrow(RuntimeException.class).when(autenticationMappers).convertToDomain(loginRequest);

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () -> {
            servizioAutenticazioneApiDelegateImpl.login(loginRequest);
        });
    }

    @Test
    void shouldVerifyOtp_whenAllOk(){
        //PARAMETERS
        VerifyOTPRequest verifyOtpRequest = new VerifyOTPRequest();
        SecondStepLoginRequest request = new SecondStepLoginRequest();
        SecondStepLoginResponse response = new SecondStepLoginResponse();
        VerifyToken200Response convertedResponse = new VerifyToken200Response();

        //MOCK
    }
}
