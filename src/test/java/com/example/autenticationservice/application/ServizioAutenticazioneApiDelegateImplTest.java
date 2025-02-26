package com.example.autenticationservice.application;

import com.example.autenticationservice.application.mapper.AutenticationMappers;
import com.example.autenticationservice.application.service.JwtServiceImpl;
import com.example.autenticationservice.domain.model.GetUsernameResponse;
import com.example.autenticationservice.domain.model.autentication.*;
import com.example.autenticationservice.domain.model.register.StepRegisterRequest;
import com.example.autenticationservice.domain.model.register.StepRegisterResponse;
import com.example.autenticationservice.domain.model.verifyToken.GetAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.verifyToken.GetAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.model.verifyToken.VerifyTokenResponse;
import com.example.autenticationservice.domain.service.AutenticationService;
import com.example.autenticationservice.generated.application.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

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
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(convertedResponse, result.getBody());

        verify(autenticationMappers).convertToDomain(registerRequest);
        verify(autenticationService).register(request);
        verify(autenticationMappers).convertFromDomain(response);
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
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(convertedResponse, result.getBody());

        verify(autenticationMappers).convertToDomain(loginRequest);
        verify(autenticationService).firstStepLogin(request);
        verify(autenticationMappers).convertFromDomain(response);
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
        VerifyOTP200Response convertedResponse = new VerifyOTP200Response();

        //MOCK
        doReturn(request).when(autenticationMappers).convertToDomain(verifyOtpRequest);
        doReturn(response).when(autenticationService).secondStepLogin(request);
        doNothing().when(jwtServiceImpl).setAuthorizationHeader(response.getAccessToken());
        doNothing().when(jwtServiceImpl).generateRefreshCookie(response.getRefreshToken());
        doReturn(convertedResponse).when(autenticationMappers).convertFromDomain(response);

        //TEST
        ResponseEntity<VerifyOTP200Response> result = servizioAutenticazioneApiDelegateImpl.verifyOTP(verifyOtpRequest);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(convertedResponse, result.getBody());

        verify(autenticationMappers).convertToDomain(verifyOtpRequest);
        verify(autenticationService).secondStepLogin(request);
        verify(jwtServiceImpl).setAuthorizationHeader(response.getAccessToken());
        verify(jwtServiceImpl).generateRefreshCookie(response.getRefreshToken());
        verify(autenticationMappers).convertFromDomain(response);
    }

    @Test
    void shouldThrowException_whenVerifyOtpRequestIsNull() {
        //TEST + RESULTS
        Assertions.assertThrows(NullPointerException.class, () ->
                servizioAutenticazioneApiDelegateImpl.verifyOTP(null)
        );
    }


    @Test
    void shouldThrowException_whenSecondStepLoginReturnsNull() {
        //PARAMETERS
        VerifyOTPRequest verifyOtpRequest = new VerifyOTPRequest();
        SecondStepLoginRequest request = new SecondStepLoginRequest();

        //MOCK
        doReturn(request).when(autenticationMappers).convertToDomain(verifyOtpRequest);
        doReturn(null).when(autenticationService).secondStepLogin(request);

        //TEST + RESULTS
        Assertions.assertThrows(NullPointerException.class, () ->
                servizioAutenticazioneApiDelegateImpl.verifyOTP(verifyOtpRequest)
        );
    }

    @Test
    void shouldThrowException_whenVerifyOtpSetAuthorizationHeaderThrowsException() {
        //PARAMETERS
        VerifyOTPRequest verifyOtpRequest = new VerifyOTPRequest();
        SecondStepLoginRequest request = new SecondStepLoginRequest();
        SecondStepLoginResponse response = new SecondStepLoginResponse();

        //MOCK
        doReturn(request).when(autenticationMappers).convertToDomain(verifyOtpRequest);
        doReturn(response).when(autenticationService).secondStepLogin(request);
        doThrow(RuntimeException.class).when(jwtServiceImpl).setAuthorizationHeader(response.getAccessToken());

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () ->
                servizioAutenticazioneApiDelegateImpl.verifyOTP(verifyOtpRequest)
        );
    }

    @Test
    void shouldThrowException_whenVerifyOtpConvertFromDomainThrowsException() {
        //PARAMETERS
        VerifyOTPRequest verifyOtpRequest = new VerifyOTPRequest();
        SecondStepLoginRequest request = new SecondStepLoginRequest();
        SecondStepLoginResponse response = new SecondStepLoginResponse();

        //MOCK
        doReturn(request).when(autenticationMappers).convertToDomain(verifyOtpRequest);
        doReturn(response).when(autenticationService).secondStepLogin(request);
        doThrow(RuntimeException.class).when(autenticationMappers).convertFromDomain(response);

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () ->
                servizioAutenticazioneApiDelegateImpl.verifyOTP(verifyOtpRequest)
        );
    }

    @Test
    void shouldReSendOtp_whenAllOk(){
        //PARAMETERS
        ReSendOtpRequest reSendOtpRequest = new ReSendOtpRequest();
        ResendOtpRequest request = new ResendOtpRequest();
        ResendOtpResponse response = new ResendOtpResponse();
        ReSendOtp200Response convertedResponse = new ReSendOtp200Response();

        //MOCK
        doReturn(request).when(autenticationMappers).convertToDomain(reSendOtpRequest);
        doReturn(response).when(autenticationService).resendOtp(request);
        doReturn(convertedResponse).when(autenticationMappers).convertFromDomain(response);

        //TEST
        ResponseEntity<ReSendOtp200Response> result = servizioAutenticazioneApiDelegateImpl.reSendOtp(reSendOtpRequest);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(convertedResponse, result.getBody());

        verify(autenticationMappers).convertToDomain(reSendOtpRequest);
        verify(autenticationService).resendOtp(request);
        verify(autenticationMappers).convertFromDomain(response);
    }

    @Test
    void shouldThrowException_whenReSendOtpFail(){
        //PARAMETERS
        ReSendOtpRequest reSendOtpRequest = new ReSendOtpRequest();

        //MOCK
        doThrow(RuntimeException.class).when(autenticationMappers).convertToDomain(reSendOtpRequest);

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () -> {
            servizioAutenticazioneApiDelegateImpl.reSendOtp(reSendOtpRequest);
        });
    }

    @Test
    void shouldVerifyToken_whenAllOk(){
        //PARAMETERS
        VerifyTokenResponse response = new VerifyTokenResponse();
        VerifyToken200Response convertedResponse = new VerifyToken200Response();

        //MOCK
        doReturn(response).when(autenticationService).verifyToken();
        doReturn(convertedResponse).when(autenticationMappers).convertFromDomain(response);

        //TEST
        ResponseEntity<VerifyToken200Response> result = servizioAutenticazioneApiDelegateImpl.verifyToken();

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(convertedResponse, result.getBody());

        verify(autenticationMappers).convertFromDomain(response);
        verify(autenticationService).verifyToken();
        verify(autenticationMappers).convertFromDomain(response);
    }

    @Test
    void shouldThrowException_whenVerifyTokenFails() {
        //MOCK
        doThrow(RuntimeException.class).when(autenticationService).verifyToken();

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () ->
                servizioAutenticazioneApiDelegateImpl.verifyToken()
        );

        verify(autenticationService).verifyToken();
        verifyNoMoreInteractions(autenticationService, autenticationMappers);
    }

    @Test
    void shouldRefreshToken_whenAllOk(){
        //PARAMETERS
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        GetAccessTokenByRefreshTokenRequest request = new GetAccessTokenByRefreshTokenRequest();
        GetAccessTokenByRefreshTokenResponse response = new GetAccessTokenByRefreshTokenResponse();
        RefreshToken200Response convertedResponse = new RefreshToken200Response();

        //MOCK
        doReturn(request).when(autenticationMappers).convertToDomain(refreshTokenRequest);
        doReturn(response).when(autenticationService).getNewAccessToken(request);
        doNothing().when(jwtServiceImpl).setAuthorizationHeader(response.getAccessToken());
        doReturn(convertedResponse).when(autenticationMappers).convertFromDomain(response);

        //TEST
        ResponseEntity<RefreshToken200Response> result = servizioAutenticazioneApiDelegateImpl.refreshToken(refreshTokenRequest);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(convertedResponse, result.getBody());

        verify(autenticationMappers).convertToDomain(refreshTokenRequest);
        verify(autenticationService).getNewAccessToken(request);
        verify(jwtServiceImpl).setAuthorizationHeader(response.getAccessToken());
        verify(autenticationMappers).convertFromDomain(response);
    }

    @Test
    void shouldThrowException_whenRefreshTokenRequestIsNull() {
        //TEST + RESULTS
        Assertions.assertThrows(NullPointerException.class, () ->
                servizioAutenticazioneApiDelegateImpl.refreshToken(null)
        );
    }


    @Test
    void shouldThrowException_whenGetAccessTokenByRefreshTokenReturnsNull() {
        //PARAMETERS
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        GetAccessTokenByRefreshTokenRequest request = new GetAccessTokenByRefreshTokenRequest();

        //MOCK
        doReturn(request).when(autenticationMappers).convertToDomain(refreshTokenRequest);
        doReturn(null).when(autenticationService).getNewAccessToken(request);

        //TEST + RESULTS
        Assertions.assertThrows(NullPointerException.class, () ->
                servizioAutenticazioneApiDelegateImpl.refreshToken(refreshTokenRequest)
        );
    }

    @Test
    void shouldThrowException_whenRefreshTokenSetAuthorizationHeaderThrowsException() {
        //PARAMETERS
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        GetAccessTokenByRefreshTokenRequest request = new GetAccessTokenByRefreshTokenRequest();
        GetAccessTokenByRefreshTokenResponse response = new GetAccessTokenByRefreshTokenResponse();

        //MOCK
        doReturn(request).when(autenticationMappers).convertToDomain(refreshTokenRequest);
        doReturn(response).when(autenticationService).getNewAccessToken(request);
        doThrow(RuntimeException.class).when(jwtServiceImpl).setAuthorizationHeader(response.getAccessToken());

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () ->
                servizioAutenticazioneApiDelegateImpl.refreshToken(refreshTokenRequest)
        );
    }

    @Test
    void shouldThrowException_whenRefreshTokenConvertFromDomainThrowsException() {
        //PARAMETERS
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        GetAccessTokenByRefreshTokenRequest request = new GetAccessTokenByRefreshTokenRequest();
        GetAccessTokenByRefreshTokenResponse response = new GetAccessTokenByRefreshTokenResponse();

        //MOCK
        doReturn(request).when(autenticationMappers).convertToDomain(refreshTokenRequest);
        doReturn(response).when(autenticationService).getNewAccessToken(request);
        doThrow(RuntimeException.class).when(autenticationMappers).convertFromDomain(response);

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () ->
                servizioAutenticazioneApiDelegateImpl.refreshToken(refreshTokenRequest)
        );
    }

    @Test
    void shouldLogout_whenAllOk(){
        //PARAMETERS
        LogoutResponse response = new LogoutResponse();
        ResponseCookie cleanRefreshCookie = ResponseCookie.from("token", "").build();
        Logout200Response convertedResponse = new Logout200Response();

        //MOCK
        doReturn(response).when(autenticationService).logout();
        doReturn(cleanRefreshCookie).when(jwtServiceImpl).getCleanJwtCookie();
        doReturn(convertedResponse).when(autenticationMappers).convertFromDomain(response);

        //TEST
        ResponseEntity<Logout200Response> result = servizioAutenticazioneApiDelegateImpl.logout();

        //VERIFY
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(convertedResponse, result.getBody());

        Assertions.assertTrue(result.getHeaders().containsKey(HttpHeaders.SET_COOKIE));
        Assertions.assertEquals(cleanRefreshCookie.toString(), result.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        Assertions.assertEquals("", result.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

        verify(autenticationMappers).convertFromDomain(response);
        verify(autenticationService).logout();
        verify(jwtServiceImpl).getCleanJwtCookie();
    }

    @Test
    void shouldThrowException_whenLogoutFails() {
        //MOCK
        doThrow(RuntimeException.class).when(autenticationService).logout();

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () -> servizioAutenticazioneApiDelegateImpl.logout());

        verify(autenticationService).logout();
    }

    @Test
    void shouldThrowException_whenLogoutJwtCookieFails() {
        //PARAMETERS
        LogoutResponse response = new LogoutResponse();

        //MOCK
        doReturn(response).when(autenticationService).logout();
        doThrow(RuntimeException.class).when(jwtServiceImpl).getCleanJwtCookie();

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () -> servizioAutenticazioneApiDelegateImpl.logout());

        verify(autenticationService).logout();
        verify(jwtServiceImpl).getCleanJwtCookie();
    }

    @Test
    void shouldThrowException_whenLogoutResponseConversionFails() {
        //PARAMETERS
        LogoutResponse response = new LogoutResponse();
        ResponseCookie cleanRefreshCookie = ResponseCookie.from("token", "").build();

        //MOCK
        doReturn(response).when(autenticationService).logout();
        doReturn(cleanRefreshCookie).when(jwtServiceImpl).getCleanJwtCookie();
        doThrow(RuntimeException.class).when(autenticationMappers).convertFromDomain(response);

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () -> servizioAutenticazioneApiDelegateImpl.logout());

        verify(autenticationService).logout();
        verify(jwtServiceImpl).getCleanJwtCookie();
        verify(autenticationMappers).convertFromDomain(response);
    }

    @Test
    void shouldReturnUsernameList_whenAllOk(){
        //PARAMETERS
        List<GetUsernameResponse> response =  List.of(new GetUsernameResponse());
        List<GetUsernameList200ResponseInner> convertedResponse = List.of(new GetUsernameList200ResponseInner());

        //MOCK
        doReturn(response).when(autenticationService).getUsername();
        doReturn(convertedResponse.get(0)).when(autenticationMappers).convertFromDomain(response.get(0));

        //TEST
        ResponseEntity<List<GetUsernameList200ResponseInner>> result = servizioAutenticazioneApiDelegateImpl.getUsernameList();

        //RESULT
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(convertedResponse, result.getBody());

        verify(autenticationService).getUsername();
    }

    @Test
    void shouldThrowException_whenGetUsernameListFails() {
        //MOCK
        doThrow(RuntimeException.class).when(autenticationService).getUsername();

        //TEST + RESULT
        Assertions.assertThrows(RuntimeException.class, () -> servizioAutenticazioneApiDelegateImpl.getUsernameList());

        verify(autenticationService).getUsername();
        verifyNoMoreInteractions(autenticationService, autenticationMappers);
    }

    @Test
    void shouldThrowException_whenGetUsernameListConversionFails() {
        //PARAMETERS
        List<GetUsernameResponse> response = List.of(new GetUsernameResponse());

        //MOCK
        doReturn(response).when(autenticationService).getUsername();
        doThrow(RuntimeException.class).when(autenticationMappers).convertFromDomain(any(GetUsernameResponse.class));

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () -> servizioAutenticazioneApiDelegateImpl.getUsernameList());

        verify(autenticationService).getUsername();
        verify(autenticationMappers).convertFromDomain(any(GetUsernameResponse.class));
        verifyNoMoreInteractions(autenticationService, autenticationMappers);
    }
}
