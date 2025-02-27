package com.example.autenticationservice.application.mapper;

import com.example.autenticationservice.domain.model.GetUsernameResponse;
import com.example.autenticationservice.domain.model.autentication.*;
import com.example.autenticationservice.domain.model.register.StepRegisterRequest;
import com.example.autenticationservice.domain.model.register.StepRegisterResponse;
import com.example.autenticationservice.domain.model.verifyToken.GetAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.verifyToken.GetAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.model.verifyToken.VerifyTokenResponse;
import com.example.autenticationservice.generated.application.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(MockitoExtension.class)
public class AutenticationMappersImplTest {

    @InjectMocks
    private AutenticationMappersImpl autenticationMappersImpl;

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
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUsername");
        loginRequest.setPassword("testPassword");

        //TEST
        FirstStepLoginRequest result = autenticationMappersImpl.convertToDomain(loginRequest);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals("testUsername",result.getUsername());
        Assertions.assertEquals("testPassword",result.getPassword());
    }

    @Test
    void shouldReturnNullFirstStepRegisterRequest_whenResponseIsNull(){
        //PARAMETERS
        LoginRequest loginRequest = null;

        //TEST
        FirstStepLoginRequest result = autenticationMappersImpl.convertToDomain(loginRequest);

        //RESULTS
        Assertions.assertNull(result);
    }

    @Test
    void shouldConvertFromDomainLogin200Response_whenAllOk(){
        //PARAMETERS
        FirstStepLoginResponse firstStepLoginResponse = new FirstStepLoginResponse();
        firstStepLoginResponse.setMessage("Success");

        //TEST
        Login200Response result = autenticationMappersImpl.convertFromDomain(firstStepLoginResponse);

        //RESULTS
        Assertions.assertNotNull(result);
    }

    @Test
    void shouldReturnNullLogin200Response_whenResponseIsNull(){
        //PARAMETERS
        FirstStepLoginResponse firstStepLoginResponse = null;

        //TEST
        Login200Response result = autenticationMappersImpl.convertFromDomain(firstStepLoginResponse);

        //RESULTS
        Assertions.assertNull(result);
    }

    @Test
    void shouldConvertToDomainSecondStepLoginRequest_whenAllOk(){
        //PARAMETERS
        VerifyOTPRequest verifyOTPRequest = new VerifyOTPRequest();
        verifyOTPRequest.setOtp("123456");
        verifyOTPRequest.setSessionId("12345");
        verifyOTPRequest.setUsername("testUsername");

        //TEST
        SecondStepLoginRequest result = autenticationMappersImpl.convertToDomain(verifyOTPRequest);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals("123456",result.getOtp());
        Assertions.assertEquals("12345",result.getSessionId());
        Assertions.assertEquals("testUsername",result.getUsername());
    }

    @Test
    void shouldReturnNullSecondStepLoginRequest_whenResponseIsNull(){
        //PARAMETERS
        VerifyOTPRequest verifyOTPRequest = null;

        //TEST
        SecondStepLoginRequest result = autenticationMappersImpl.convertToDomain(verifyOTPRequest);

        //RESULTS
        Assertions.assertNull(result);
    }

    @Test
    void shouldConvertFromDomainVerifyOTP200Response_whenAllOk(){
        //PARAMETERS
        SecondStepLoginResponse secondStepLoginResponse = new SecondStepLoginResponse();
        secondStepLoginResponse.setAccessToken("accessToken");
        secondStepLoginResponse.setRefreshToken("refreshToken");

        //TEST
        VerifyOTP200Response result = autenticationMappersImpl.convertFromDomain(secondStepLoginResponse);

        //RESULTS
        Assertions.assertNotNull(result);
    }

    @Test
    void shouldReturnNullSVerifyOTP200Response_whenResponseIsNull(){
        //PARAMETERS
        SecondStepLoginResponse secondStepLoginResponse = null;

        //TEST
        VerifyOTP200Response result = autenticationMappersImpl.convertFromDomain(secondStepLoginResponse);

        //RESULTS
        Assertions.assertNull(result);
    }

    @Test
    void shouldConvertToDomainResendOtpRequest_whenAllOk(){
        //PARAMETERS
        ReSendOtpRequest reSendOtpRequest = new ReSendOtpRequest();
        reSendOtpRequest.setSessionId("12345");
        reSendOtpRequest.setUsername("testUsername");

        //TEST
        ResendOtpRequest result = autenticationMappersImpl.convertToDomain(reSendOtpRequest);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals("12345",result.getSessionId());
        Assertions.assertEquals("testUsername",result.getUsername());
    }

    @Test
    void shouldReturnNullResendOtpRequest_whenResponseIsNull(){
        //PARAMETERS
        ReSendOtpRequest reSendOtpRequest = null;

        //TEST
        ResendOtpRequest result = autenticationMappersImpl.convertToDomain(reSendOtpRequest);

        //RESULTS
        Assertions.assertNull(result);
    }

    @Test
    void shouldConvertFromDomainReSendOtp200Response_whenAllOk(){
        //PARAMETERS
        ResendOtpResponse resendOtpResponse = new ResendOtpResponse();
        resendOtpResponse.setMessage("Success");

        //TEST
        ReSendOtp200Response result =  autenticationMappersImpl.convertFromDomain(resendOtpResponse);

        //RESULTS
        Assertions.assertNotNull(result);
    }

    @Test
    void shouldReturnNullResendOtp200Response_whenResponseIsNull(){
        //PARAMETERS
        ResendOtpResponse resendOtpResponse = null;

        //TEST
        ReSendOtp200Response result =  autenticationMappersImpl.convertFromDomain(resendOtpResponse);

        //RESULTS
        Assertions.assertNull(result);
    }

    @Test
    void shouldConvertFromDomainVerifyToken200Response_whenAllOk(){
        //PARAMETERS
        VerifyTokenResponse verifyTokenResponse = new VerifyTokenResponse();
        verifyTokenResponse.setUsername("testUsername");

        //TEST
        VerifyToken200Response result = autenticationMappersImpl.convertFromDomain(verifyTokenResponse);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals("testUsername",result.getUsername());
    }

    @Test
    void shouldReturnNullVerifyToken200Response_whenResponseIsNull(){
        //PARAMETERS
        VerifyTokenResponse verifyTokenResponse = null;

        //TEST
        VerifyToken200Response result = autenticationMappersImpl.convertFromDomain(verifyTokenResponse);

        //RESULTS
        Assertions.assertNull(result);
    }

    @Test
    void shouldConvertToDomainGetAccessTokenByRefreshTokenRequest_whenAllOk(){
        //PARAMETERS
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("refreshToken");

        //TEST
        GetAccessTokenByRefreshTokenRequest result = autenticationMappersImpl.convertToDomain(refreshTokenRequest);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals("refreshToken",result.getRefreshToken());
    }

    @Test
    void shouldReturnNullGetAccessTokenByRefreshTokenRequest_whenResponseIsNull(){
        //PARAMETERS
        RefreshTokenRequest refreshTokenRequest = null;

        //TEST
        GetAccessTokenByRefreshTokenRequest result = autenticationMappersImpl.convertToDomain(refreshTokenRequest);

        //RESULTS
        Assertions.assertNull(result);
    }

    @Test
    void shouldConvertFromDomainRefreshToken200Response_whenAllOk(){
        //PARAMETERS
        GetAccessTokenByRefreshTokenResponse getAccessTokenByRefreshTokenResponse = new GetAccessTokenByRefreshTokenResponse();
        getAccessTokenByRefreshTokenResponse.setAccessToken("accessToken");
        getAccessTokenByRefreshTokenResponse.setMessage("success");

        //TEST
        RefreshToken200Response result = autenticationMappersImpl.convertFromDomain(getAccessTokenByRefreshTokenResponse);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals("accessToken",result.getAccessToken());
        Assertions.assertEquals("success",result.getMessage());
    }

    @Test
    void shouldReturnNullRefreshToken200Response_whenResponseIsNull(){
        //PARAMETERS
        GetAccessTokenByRefreshTokenResponse getAccessTokenByRefreshTokenResponse = null;

        //TEST
        RefreshToken200Response result = autenticationMappersImpl.convertFromDomain(getAccessTokenByRefreshTokenResponse);

        //RESULTS
        Assertions.assertNull(result);
    }

    @Test
    void shouldConvertFromDomainLogout200Response_whenAllOk(){
        //PARAMETERS
        LogoutResponse logoutResponse = new LogoutResponse();
        logoutResponse.setMessage("success");

        //TEST
        Logout200Response result = autenticationMappersImpl.convertFromDomain(logoutResponse);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals("success", result.getMessage());
    }

    @Test
    void shouldReturnNullLogout200Response_whenResponseIsNull(){
        //PARAMETERS
        LogoutResponse logoutResponse = null;

        //TEST
        Logout200Response result = autenticationMappersImpl.convertFromDomain(logoutResponse);

        //RESULTS
        Assertions.assertNull(result);
    }

    @Test
    void shouldConvertFromDomainGetUsernameList200ResponseInner_whenAllOk(){
        //PARAMETERS
        GetUsernameResponse getUsernameResponse = new GetUsernameResponse();
        getUsernameResponse.setUsername("usernameTest");
        getUsernameResponse.getUsername();

        //TEST
        GetUsernameList200ResponseInner result = autenticationMappersImpl.convertFromDomain(getUsernameResponse);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals("usernameTest",result.getUsername());
    }

    @Test
    void shouldReturnNullGetUsernameList200ResponseInner_whenResponseIsNull(){
        //PARAMETERS
        GetUsernameResponse getUsernameResponse = null;

        //TEST
        GetUsernameList200ResponseInner result = autenticationMappersImpl.convertFromDomain(getUsernameResponse);

        //RESULTS
        Assertions.assertNull(result);
    }
}
