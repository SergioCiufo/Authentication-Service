package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.api.EmailService;
import com.example.autenticationservice.domain.api.JwtService;
import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.model.autentication.FirstStepLoginRequest;
import com.example.autenticationservice.domain.model.autentication.FirstStepLoginResponse;
import com.example.autenticationservice.domain.model.register.StepRegisterRequest;
import com.example.autenticationservice.domain.model.register.StepRegisterResponse;
import com.example.autenticationservice.domain.util.HashUtil;
import com.example.autenticationservice.domain.util.OtpUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.UUID;

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




}
