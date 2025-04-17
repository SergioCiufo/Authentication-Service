package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.api.EmailService;
import com.example.autenticationservice.domain.api.JwtService;
import com.example.autenticationservice.domain.exceptions.ExpireOtpException;
import com.example.autenticationservice.domain.exceptions.InvalidCredentialsException;
import com.example.autenticationservice.domain.exceptions.MissingTokenException;
import com.example.autenticationservice.domain.exceptions.TokenExpiredException;
import com.example.autenticationservice.domain.model.GetUsernameResponse;
import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.model.autentication.*;
import com.example.autenticationservice.domain.model.register.StepRegisterRequest;
import com.example.autenticationservice.domain.model.register.StepRegisterResponse;
import com.example.autenticationservice.domain.model.verifyToken.GetAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.verifyToken.GetAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.model.verifyToken.VerifyTokenResponse;
import com.example.autenticationservice.domain.util.HashUtil;
import com.example.autenticationservice.domain.util.OtpUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.util.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AutenticationServiceImplTest {
    @InjectMocks
    private AutenticationServiceImpl autenticationServiceImpl;

    @Mock
    private EmailService emailService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private OtpService otpService;

    @Mock
    private TokenService tokenService;

    @Mock
    private OtpUtil otpUtil;

    @Mock
    private HashUtil hashUtil;

    @Test
    public void shouldRegister_whenAllOk() {
        //PARAMETERS
        StepRegisterRequest stepRegisterRequest = StepRegisterRequest.builder()
                .name("nameTest")
                .username("usernameTest")
                .email("emailTest")
                .password("pswTest")
                .build();

        String passwordSha1 = "hashedPassword";

        User user = User.builder()
                .name("nameTest")
                .username("usernameTest")
                .password(passwordSha1)
                .email("emailTest")
                .otpList(new ArrayList<>())
                .build();

        //MOCK
        doReturn(passwordSha1).when(hashUtil).stringToSha1(stepRegisterRequest.getPassword());
        doNothing().when(userService).register(user);

        //TEST
        StepRegisterResponse result = autenticationServiceImpl.register(stepRegisterRequest);

        //RISULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Registration completed", result.getMessage());
        verify(userService, times(1)).register(user);
    }

    @Test
    public void shouldFailRegister_whenEmailAlreadyExists() {
        //PARAMETERS
        StepRegisterRequest stepRegisterRequest = StepRegisterRequest.builder()
                .name("nameTest")
                .username("usernameTest")
                .email("emailTest")
                .password("pswTest")
                .build();

        String passwordSha1 = "hashedPassword";

        User user = User.builder()
                .name("nameTest")
                .username("usernameTest")
                .password(passwordSha1)
                .email("emailTest")
                .otpList(new ArrayList<>())
                .build();

        //MOCK
        doReturn(passwordSha1).when(hashUtil).stringToSha1(stepRegisterRequest.getPassword());
        doThrow(RuntimeException.class).when(userService).register(user);

        //TEST
        Assertions.assertThrows(RuntimeException.class, () -> autenticationServiceImpl.register(stepRegisterRequest));

        //RISULTS
        verify(userService, times(1)).register(user);
    }

    @Test
    public void shouldFirstStepLogin_whenAllOk() {
        // PARAMETERS
        FirstStepLoginRequest stepLoginRequest = FirstStepLoginRequest.builder()
                .username("usernameTest")
                .password("pswTest")
                .build();

        String passwordSha1 = "hashedPassword";

        User user = User.builder()
                .name("nameTest")
                .username("usernameTest")
                .password(passwordSha1)
                .email("emailTest")
                .otpList(new ArrayList<>())
                .build();

        Otp otp = Otp.builder()
                .user(user)
                .sessionId("sessionIdRandom")
                .otp("123456")
                .build();

        //MOCK
        doReturn(passwordSha1).when(hashUtil).stringToSha1(stepLoginRequest.getPassword());
        doReturn(user).when(userService).getUserByUsernameAndPassword(stepLoginRequest.getUsername(), passwordSha1);
        doReturn(otp).when(otpUtil).generateOtp(eq(user), any());  //qualsiasi sessionId
        doNothing().when(otpService).saveOtp(otp);
        doNothing().when(emailService).sendEmail(user.getEmail(), "Chat4Me - OTP code", otp.getOtp());

        //TEST
        FirstStepLoginResponse result = autenticationServiceImpl.firstStepLogin(stepLoginRequest);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Login successful, OTP sent", result.getMessage());
        verify(userService, times(1)).getUserByUsernameAndPassword(stepLoginRequest.getUsername(), passwordSha1);
        verify(otpUtil, times(1)).generateOtp(eq(user), any()); //con qualsiasi sessionId
        verify(otpService, times(1)).saveOtp(otp);
        verify(emailService, times(1)).sendEmail(user.getEmail(), "Chat4Me - OTP code", otp.getOtp());
    }

    @Test
    public void shouldFirstStepLogin_whenUserNotFound() {
        //PARAMETERS
        FirstStepLoginRequest stepLoginRequest = FirstStepLoginRequest.builder()
                .username("invalidUsername")
                .password("pswTest")
                .build();

        String passwordSha1 = "hashedPassword";

        //MOCK
        doReturn(passwordSha1).when(hashUtil).stringToSha1(stepLoginRequest.getPassword());
        doThrow(RuntimeException.class).when(userService).getUserByUsernameAndPassword(stepLoginRequest.getUsername(), passwordSha1);

        //TEST
        Assertions.assertThrows(RuntimeException.class, () -> {
            autenticationServiceImpl.firstStepLogin(stepLoginRequest);
        });

        //RESULTS
        verify(userService, times(1)).getUserByUsernameAndPassword(stepLoginRequest.getUsername(), passwordSha1);
    }

    @Test
    public void shouldFirstStepLogin_whenOtpGenerationFails() {
        //PARAMETERS
        FirstStepLoginRequest stepLoginRequest = FirstStepLoginRequest.builder()
                .username("usernameTest")
                .password("pswTest")
                .build();

        String passwordSha1 = "hashedPassword";

        User user = User.builder()
                .name("nameTest")
                .username("usernameTest")
                .password(passwordSha1)
                .email("emailTest")
                .otpList(new ArrayList<>())
                .build();

        //MOCK
        doReturn(passwordSha1).when(hashUtil).stringToSha1(stepLoginRequest.getPassword());
        doReturn(user).when(userService).getUserByUsernameAndPassword(stepLoginRequest.getUsername(), passwordSha1);
        doThrow(RuntimeException.class).when(otpUtil).generateOtp(eq(user), any());

        //TEST
        Assertions.assertThrows(RuntimeException.class, () -> {
            autenticationServiceImpl.firstStepLogin(stepLoginRequest);
        });

        //RESULTS
        verify(userService, times(1)).getUserByUsernameAndPassword(stepLoginRequest.getUsername(), passwordSha1);
        verify(otpUtil, times(1)).generateOtp(eq(user), any());
    }

    @Test
    public void shouldFirstStepLogin_whenEmailSendingFails() {
        //PARAMETERS
        FirstStepLoginRequest stepLoginRequest = FirstStepLoginRequest.builder()
                .username("usernameTest")
                .password("pswTest")
                .build();

        String passwordSha1 = "hashedPassword";

        User user = User.builder()
                .name("nameTest")
                .username("usernameTest")
                .password(passwordSha1)
                .email("emailTest")
                .otpList(new ArrayList<>())
                .build();

        Otp otp = Otp.builder()
                .user(user)
                .sessionId("sessionIdRandom")
                .otp("123456")
                .build();

        //MOCK
        doReturn(passwordSha1).when(hashUtil).stringToSha1(stepLoginRequest.getPassword());
        doReturn(user).when(userService).getUserByUsernameAndPassword(stepLoginRequest.getUsername(), passwordSha1);
        doReturn(otp).when(otpUtil).generateOtp(eq(user), any());
        doThrow(RuntimeException.class).when(emailService).sendEmail(user.getEmail(), "Chat4Me - OTP code", otp.getOtp());

        //TEST
        Assertions.assertThrows(RuntimeException.class, () -> {
            autenticationServiceImpl.firstStepLogin(stepLoginRequest);
        });

        //RESULTS
        verify(userService, times(1)).getUserByUsernameAndPassword(stepLoginRequest.getUsername(), passwordSha1);
        verify(otpUtil, times(1)).generateOtp(eq(user), any());
        verify(emailService, times(1)).sendEmail(user.getEmail(), "Chat4Me - OTP code", otp.getOtp());
    }

    @Test
    public void shouldSecondStepLogin_whenAllOk(){
        //PARAMETERS
        SecondStepLoginRequest stepLoginRequest = SecondStepLoginRequest.builder()
                .username("usernameTest")
                .sessionId("sessionTest")
                .otp("123456")
                .build();

        User user = User.builder()
                .name("nameTest")
                .username("usernameTest")
                .password("pswTest")
                .email("emailTest")
                .otpList(new ArrayList<>())
                .build();

        Otp otp = Otp.builder()
                .otp("123456")
                .user(user)
                .sessionId("sessionTest")
                .build();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .refreshToken("refreshTokenTest")
                .build();

        String accessToken = "accessTokenTest";

        //MOCK
        doReturn(user).when(userService).getUserByUsername(stepLoginRequest.getUsername());
        doReturn(otp).when(otpService).getOtpBySessionId(stepLoginRequest.getSessionId());
        doReturn(false).when(otpUtil).checkOtpMaxAttempt(otp);
        doReturn(false).when(otpUtil).isOtpExpired(otp);
        doNothing().when(otpService).invalidateOtp(otp);
        doReturn(refreshToken).when(tokenService).generateRefreshToken(user);
        doNothing().when(tokenService).addRefreshToken(refreshToken);
        doReturn(accessToken).when(tokenService).generateAccessToken(user.getUsername());

        //TEST
        SecondStepLoginResponse secondStepLoginResponse = autenticationServiceImpl.secondStepLogin(stepLoginRequest);

        //RESULTS
        Assertions.assertNotNull(secondStepLoginResponse);
        Assertions.assertEquals(stepLoginRequest.getOtp(), otp.getOtp());
        Assertions.assertEquals(accessToken, secondStepLoginResponse.getAccessToken());
        Assertions.assertEquals(refreshToken.getRefreshToken(), secondStepLoginResponse.getRefreshToken());
        verify(userService, times(1)).getUserByUsername(stepLoginRequest.getUsername());
        verify(otpService, times(1)).getOtpBySessionId(stepLoginRequest.getSessionId());
        verify(otpUtil, times(1)).checkOtpMaxAttempt(otp);
        verify(otpUtil, times(1)).isOtpExpired(otp);
        verify(otpService, times(1)).invalidateOtp(otp);
        verify(tokenService, times(1)).generateRefreshToken(user);
        verify(tokenService, times(1)).addRefreshToken(refreshToken);
        verify(tokenService, times(1)).generateAccessToken(user.getUsername());
    }

    @Test
    public void shouldSecondStepLoginException_whenOtpIsExpiredMaxAttempts() {
        //PARAMETERS
        SecondStepLoginRequest stepLoginRequest = SecondStepLoginRequest.builder()
                .username("usernameTest")
                .sessionId("sessionTest")
                .otp("123456")
                .build();

        User user = User.builder()
                .name("nameTest")
                .username("usernameTest")
                .password("pswTest")
                .email("emailTest")
                .otpList(new ArrayList<>())
                .build();

        Otp otp = Otp.builder()
                .otp("123456")
                .user(user)
                .sessionId("sessionTest")
                .attempts(3)
                .build();

        //MOCK
        doReturn(user).when(userService).getUserByUsername(stepLoginRequest.getUsername());
        doReturn(otp).when(otpService).getOtpBySessionId(stepLoginRequest.getSessionId());
        doReturn(true).when(otpUtil).checkOtpMaxAttempt(otp);
        doNothing().when(otpService).invalidateOtp(otp);

        //TEST
        Assertions.assertThrows(ExpireOtpException.class, () -> {
            autenticationServiceImpl.secondStepLogin(stepLoginRequest);
        });

        //RESULTS
        verify(userService, times(1)).getUserByUsername(stepLoginRequest.getUsername());
        verify(otpService, times(1)).getOtpBySessionId(stepLoginRequest.getSessionId());
        verify(otpUtil, times(1)).checkOtpMaxAttempt(otp);
        verify(otpService, times(1)).invalidateOtp(otp);
    }

    @Test
    public void shouldSecondStepLoginException_whenOtpIsNotValid() {
        //PARAMETERS
        SecondStepLoginRequest stepLoginRequest = SecondStepLoginRequest.builder()
                .username("usernameTest")
                .sessionId("sessionTest")
                .otp("123456")
                .build();

        User user = User.builder()
                .name("nameTest")
                .username("usernameTest")
                .password("pswTest")
                .email("emailTest")
                .otpList(new ArrayList<>())
                .build();

        Otp otp = Otp.builder()
                .otp("678945")
                .user(user)
                .sessionId("sessionTest")
                .attempts(2)
                .build();

        //MOCK
        doReturn(user).when(userService).getUserByUsername(stepLoginRequest.getUsername());
        doReturn(otp).when(otpService).getOtpBySessionId(stepLoginRequest.getSessionId());
        doReturn(false).when(otpUtil).checkOtpMaxAttempt(otp);
        doReturn(otp).when(otpUtil).increaseOtpAttempt(otp);
        doNothing().when(otpService).updateOtp(otp);

        //TEST
        Assertions.assertThrows(InvalidCredentialsException.class, () -> {
            autenticationServiceImpl.secondStepLogin(stepLoginRequest);
        });

        //RESULTS
        Assertions.assertNotEquals(stepLoginRequest.getOtp(), otp.getOtp());
        verify(userService, times(1)).getUserByUsername(stepLoginRequest.getUsername());
        verify(otpService, times(1)).getOtpBySessionId(stepLoginRequest.getSessionId());
        verify(otpUtil, times(1)).checkOtpMaxAttempt(otp);
        verify(otpUtil, times(1)).increaseOtpAttempt(otp);
        verify(otpService, times(1)).updateOtp(otp);
    }

    @Test
    public void shouldSecondStepLoginException_whenOtpIsExpired() {
        //PARAMETERS
        SecondStepLoginRequest stepLoginRequest = SecondStepLoginRequest.builder()
                .username("usernameTest")
                .sessionId("sessionTest")
                .otp("123456")
                .build();

        User user = User.builder()
                .name("nameTest")
                .username("usernameTest")
                .password("pswTest")
                .email("emailTest")
                .otpList(new ArrayList<>())
                .build();

        Otp otp = Otp.builder()
                .otp("123456")
                .user(user)
                .sessionId("sessionTest")
                .attempts(3)
                .build();

        //MOCK
        doReturn(user).when(userService).getUserByUsername(stepLoginRequest.getUsername());
        doReturn(otp).when(otpService).getOtpBySessionId(stepLoginRequest.getSessionId());
        doReturn(false).when(otpUtil).checkOtpMaxAttempt(otp);
        doReturn(true).when(otpUtil).isOtpExpired(otp);
        doNothing().when(otpService).invalidateOtp(otp);

        //TEST
        Assertions.assertThrows(ExpireOtpException.class, () -> {
            autenticationServiceImpl.secondStepLogin(stepLoginRequest);
        });

        //RESULTS
        verify(userService, times(1)).getUserByUsername(stepLoginRequest.getUsername());
        verify(otpService, times(1)).getOtpBySessionId(stepLoginRequest.getSessionId());
        verify(otpUtil, times(1)).checkOtpMaxAttempt(otp);
        verify(otpUtil, times(1)).isOtpExpired(otp);
        verify(otpService, times(1)).invalidateOtp(otp);
    }

    @Test
    public void shouldResendOtpResponse_whenAllOk(){
        //PARAMETERS
        ResendOtpRequest resendOtpRequest = ResendOtpRequest.builder()
                .sessionId("sessionTest")
                .username("usernameTest")
                .build();

        User user = User.builder()
                .name("nameTest")
                .username("usernameTest")
                .password("pswTest")
                .email("emailTest")
                .otpList(new ArrayList<>())
                .build();

        Otp oldOtp = Otp.builder()
                .otp("123456")
                .user(user)
                .sessionId("sessionTest")
                .attempts(2)
                .build();

        Otp newotp = Otp.builder()
                .otp("678989")
                .user(user)
                .sessionId("sessionTest")
                .attempts(0)
                .build();

        //MOCK
        doReturn(user).when(userService).getUserByUsername(resendOtpRequest.getUsername());
        doReturn(oldOtp).when(otpService).getOtpBySessionId(resendOtpRequest.getSessionId());
        doNothing().when(otpService).invalidateOtp(oldOtp);
        doReturn(newotp).when(otpUtil).generateOtp(user, resendOtpRequest.getSessionId());
        doNothing().when(otpService).saveOtp(newotp);
        doNothing().when(emailService).sendEmail(newotp.getUser().getEmail(), "Chat4Me - OTP code", newotp.getOtp());

        //TEST
        ResendOtpResponse result = autenticationServiceImpl.resendOtp(resendOtpRequest);

        //RESULTS
        Assertions.assertNotNull(result);
        verify(userService, times(1)).getUserByUsername(resendOtpRequest.getUsername());
        verify(otpService, times(1)).getOtpBySessionId(resendOtpRequest.getSessionId());
        verify(otpService, times(1)).invalidateOtp(oldOtp);
        verify(otpUtil, times(1)).generateOtp(user, resendOtpRequest.getSessionId());
        verify(otpService, times(1)).saveOtp(newotp);
        verify(emailService, times(1)).sendEmail(newotp.getUser().getEmail(), "Chat4Me - OTP code", newotp.getOtp());
    }

    @Test
    public void shouldResendOtpResponseException_whenUserNotFound(){
        //PARAMETERS
        ResendOtpRequest resendOtpRequest = ResendOtpRequest.builder()
                .sessionId("sessionTest")
                .username("invalidUsername")
                .build();

        //MOCK
        doThrow(RuntimeException.class).when(userService).getUserByUsername(resendOtpRequest.getUsername());

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () -> {
            autenticationServiceImpl.resendOtp(resendOtpRequest);
        });
    }

    @Test
    public void shouldVerifyTokenResponse_whenAllOk() {
        //PARAMETERS
        String accessToken = "accessTokenTest";
        String username = "usernameTest";

        //MOCK
        doReturn(accessToken).when(jwtService).extractAccessJwt();
        try (MockedStatic<StringUtils> mockedStringUtils = mockStatic(StringUtils.class)) {
            mockedStringUtils.when(() -> StringUtils.isBlank(accessToken)).thenReturn(false);
            doReturn(true).when(tokenService).validateRefreshToken(accessToken);
            doReturn(username).when(tokenService).getUsernameFromToken(accessToken);

            //TEST
            VerifyTokenResponse result = autenticationServiceImpl.verifyToken();

            //RESULTS
            Assertions.assertNotNull(result);
            Assertions.assertEquals(username, result.getUsername());
            verify(jwtService, times(1)).extractAccessJwt();
            verify(tokenService, times(1)).validateRefreshToken(accessToken);
            verify(tokenService, times(1)).getUsernameFromToken(accessToken);
        }
    }

    @Test
    public void shouldVerifyTokenResponseException_whenMissingAccessToken() {
        //PARAMETERS
        String accessToken = "accessTokenTest";

        //MOCK
        doReturn(accessToken).when(jwtService).extractAccessJwt();
        try (MockedStatic<StringUtils> mockedStringUtils = mockStatic(StringUtils.class)) {
            mockedStringUtils.when(() -> StringUtils.isBlank(accessToken)).thenReturn(true);

            //TEST + RESULTS
            Assertions.assertThrows(MissingTokenException.class, () -> {
                autenticationServiceImpl.verifyToken();
            });
        }
    }

    @Test
    public void shouldVerifyTokenResponseException_whenTokenExpired() {
        //PARAMETERS
        String accessToken = "accessTokenTest";

        //MOCK
        doReturn(accessToken).when(jwtService).extractAccessJwt();
        try (MockedStatic<StringUtils> mockedStringUtils = mockStatic(StringUtils.class)) {
            mockedStringUtils.when(() -> StringUtils.isBlank(accessToken)).thenReturn(false);
            doThrow(ExpiredJwtException.class).when(tokenService).validateRefreshToken(accessToken);

            //TEST + RESULTS
            Assertions.assertThrows(TokenExpiredException.class, () -> {
                autenticationServiceImpl.verifyToken();
            });

            //RESULTS
            verify(jwtService, times(1)).extractAccessJwt();
            verify(tokenService, times(1)).validateRefreshToken(accessToken);
        }
    }

    @Test
    public void shouldGetAccessTokenByRefreshTokenResponse_whenAllOk() {
        //PARAMETERS
        GetAccessTokenByRefreshTokenRequest getAccessTokenByRefreshTokenRequest = GetAccessTokenByRefreshTokenRequest.builder()
                .refreshToken("refreshTokenTest")
                .build();

        String refreshTokenString = "refreshTokenTest";

        User user = User.builder()
                .name("nameTest")
                .username("usernameTest")
                .password("pswTest")
                .email("emailTest")
                .otpList(new ArrayList<>())
                .build();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .refreshToken("refreshTokenTest")
                .build();

        String accessToken = "accessTokenTest";

        //MOCK
        try (MockedStatic<StringUtils> mockedStringUtils = mockStatic(StringUtils.class)) {
            mockedStringUtils.when(() -> StringUtils.isBlank(refreshTokenString)).thenReturn(false);
            doReturn(refreshToken).when(tokenService).getRefreshToken(refreshTokenString);
            doReturn(true).when(tokenService).validateRefreshToken(refreshTokenString);
            doReturn(accessToken).when(tokenService).generateAccessToken(refreshToken.getUser().getUsername());

            //TEST
            GetAccessTokenByRefreshTokenResponse result = autenticationServiceImpl.getNewAccessToken(getAccessTokenByRefreshTokenRequest);

            //RESULTS
            Assertions.assertNotNull(result);
            Assertions.assertEquals(accessToken, result.getAccessToken());

        }
    }

    @Test
    public void shouldGetAccessTokenByRefreshTokenResponseException_whenMissingRefreshToken() {
        //PARAMETERS
        GetAccessTokenByRefreshTokenRequest getAccessTokenByRefreshTokenRequest = GetAccessTokenByRefreshTokenRequest.builder()
                .refreshToken("refreshTokenTest")
                .build();

        String refreshTokenString = "refreshTokenTest";

        //MOCK
        try (MockedStatic<StringUtils> mockedStringUtils = mockStatic(StringUtils.class)) {
            mockedStringUtils.when(() -> StringUtils.isBlank(refreshTokenString)).thenReturn(true);

            //TEST + RESULTS
            Assertions.assertThrows(MissingTokenException.class, () -> {
                autenticationServiceImpl.getNewAccessToken(getAccessTokenByRefreshTokenRequest);
            });
        }
    }

    @Test
    public void shouldGetAccessTokenByRefreshTokenResponseException_whenInvalidRefreshToken() {
        //PARAMETERS
        GetAccessTokenByRefreshTokenRequest getAccessTokenByRefreshTokenRequest = GetAccessTokenByRefreshTokenRequest.builder()
                .refreshToken("refreshTokenTest")
                .build();

        String refreshTokenString = "refreshTokenTest";

        User user = User.builder()
                .name("nameTest")
                .username("usernameTest")
                .password("pswTest")
                .email("emailTest")
                .otpList(new ArrayList<>())
                .build();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .refreshToken("refreshTokenTest")
                .build();

        //MOCK
        try (MockedStatic<StringUtils> mockedStringUtils = mockStatic(StringUtils.class)) {
            mockedStringUtils.when(() -> StringUtils.isBlank(refreshTokenString)).thenReturn(false);
            doReturn(refreshToken).when(tokenService).getRefreshToken(refreshTokenString);
            doReturn(false).when(tokenService).validateRefreshToken(refreshTokenString);

            //TEST + RESULTS
            Assertions.assertThrows(MissingTokenException.class, () -> {
                autenticationServiceImpl.getNewAccessToken(getAccessTokenByRefreshTokenRequest);
            });

            verify(tokenService, times(1)).getRefreshToken(refreshTokenString);
            verify(tokenService, times(1)).validateRefreshToken(refreshTokenString);
            mockedStringUtils.verify(() -> StringUtils.isBlank(refreshTokenString), times(1));
        }
    }

    @Test
    public void shouldListGetUsernameResponse_whenAllOk() {
        //PARAMETERS
        User user1 = User.builder()
                .name("nameTest1")
                .username("usernameTest1")
                .password("pswTest1")
                .email("emailTest1")
                .otpList(new ArrayList<>())
                .build();

        User user2 = User.builder()
                .name("nameTest2")
                .username("usernameTest2")
                .password("pswTest2")
                .email("emailTest2")
                .otpList(new ArrayList<>())
                .build();

        List<User> users = Arrays.asList(user1, user2);

        //MOCK
        doReturn(users).when(userService).getUserList();

        //TEST
        List<GetUsernameResponse> result = autenticationServiceImpl.getUsername();

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(users.size(), result.size());
        verify(userService, times(1)).getUserList();
    }

    @Test
    public void shouldThrowException_whenGetUserListFails() {

        //MOCK
        doThrow(RuntimeException.class).when(userService).getUserList();

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () -> {
            autenticationServiceImpl.getUsername();
        });
        verify(userService, times(1)).getUserList();
    }

    @Test
    public void shouldLogoutAndInvalidateToken_whenRefreshTokenExists() {
        //PARAMETERS
        String refreshToken = "valid.refresh.token";
        FirstStepLogoutRequest request = FirstStepLogoutRequest.builder()
                .refreshToken(refreshToken)
                .build();

        LogoutResponse expectedResponse = LogoutResponse.builder()
                .message("Logout successful. Tokens invalidated.")
                .build();

        //MOCK
        doNothing().when(tokenService).invalidateRefreshToken(refreshToken);

        //TEST
        LogoutResponse result = autenticationServiceImpl.logout(request);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedResponse.getMessage(), result.getMessage());
        verify(tokenService, times(1)).invalidateRefreshToken(refreshToken);
    }

    @Test
    public void shouldLogoutWithoutInvalidating_whenRefreshTokenIsBlank() {
        //PARAMETERS
        FirstStepLogoutRequest request = FirstStepLogoutRequest.builder()
                .refreshToken("")
                .build();

        LogoutResponse expectedResponse = LogoutResponse.builder()
                .message("Logout successful. Tokens invalidated.")
                .build();

        //TEST
        LogoutResponse result = autenticationServiceImpl.logout(request);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedResponse.getMessage(), result.getMessage());
        verify(tokenService, never()).invalidateRefreshToken(any());
    }

    @Test
    public void shouldLogoutWithoutInvalidating_whenRefreshTokenIsNull() {
        //PARAMETERS
        FirstStepLogoutRequest request = FirstStepLogoutRequest.builder()
                .refreshToken(null)
                .build();

        LogoutResponse expectedResponse = LogoutResponse.builder()
                .message("Logout successful. Tokens invalidated.")
                .build();

        //TEST
        LogoutResponse result = autenticationServiceImpl.logout(request);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedResponse.getMessage(), result.getMessage());
        verify(tokenService, never()).invalidateRefreshToken(any());
    }

}
