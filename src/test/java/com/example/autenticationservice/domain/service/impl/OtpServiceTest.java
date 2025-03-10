package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.exceptions.InvalidSessionException;
import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.repository.OtpServiceRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OtpServiceTest {

    @InjectMocks
    private OtpService otpService;

    @Mock
    private OtpServiceRepo otpServiceRepo;

    @Test
    public void shouldGetOtpBySessionId_whenAllOk() {
        //PARAMETERS
        String sessionId = "sessionTest";
        User user = new User();
        Otp otp = Otp.builder()
                .id(null)
                .user(user)
                .otp("otpTest")
                .sessionId(sessionId)
                .createdAt(100)
                .expiresAt(200)
                .attempts(0)
                .valid(true)
                .build();

        //MOCK
        doReturn(Optional.of(otp)).when(otpServiceRepo).getValidOtpBySessionId(sessionId);

        //TEST
        Otp result = otpService.getOtpBySessionId(sessionId);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(otp.getOtp(), result.getOtp());
    }

    @Test
    public void shouldThrowInvalidSessionException_whenInvalidSessionId() {
        //PARAMETERS
        String sessionId = "invalidSession";

        //MOCK
        doReturn(Optional.empty()).when(otpServiceRepo).getValidOtpBySessionId(sessionId);

        //TEST + RESULTS
        Assertions.assertThrows(InvalidSessionException.class, () -> otpService.getOtpBySessionId(sessionId));
    }

    @Test
    public void shouldSaveOtp_whenAllOk() {
        //PARAMETERS
        String sessionId = "sessionTest";
        User user = new User();
        Otp otp = Otp.builder()
                .id(null)
                .user(user)
                .otp("otpTest")
                .sessionId(sessionId)
                .createdAt(100)
                .expiresAt(200)
                .attempts(0)
                .valid(true)
                .build();

        //MOCK
        doNothing().when(otpServiceRepo).saveOtp(otp);

        //TEST
        otpService.saveOtp(otp);

        //RESULTS
        Assertions.assertNotNull(otp);
        verify(otpServiceRepo, times(1)).saveOtp(otp);
    }

    @Test
    public void shouldUpdateOtp_whenAllOk() {
        //PARAMETERS
        String sessionId = "sessionTest";
        User user = new User();
        Otp otp = Otp.builder()
                .id(null)
                .user(user)
                .otp("otpTest")
                .sessionId(sessionId)
                .createdAt(100)
                .expiresAt(200)
                .attempts(0)
                .valid(true)
                .build();

        //MOCK
        doNothing().when(otpServiceRepo).updateOtp(otp);

        //TEST
        otpService.updateOtp(otp);

        //RESULTS
        Assertions.assertNotNull(otp);
        verify(otpServiceRepo, times(1)).updateOtp(otp);
    }

    @Test
    public void shouldInvalidateOtp_whenAllOk() {
        //PARAMETERS
        String sessionId = "sessionTest";
        User user = new User();
        Otp otp = Otp.builder()
                .id(null)
                .user(user)
                .otp("otpTest")
                .sessionId(sessionId)
                .createdAt(100)
                .expiresAt(200)
                .attempts(0)
                .valid(true)
                .build();

        //MOCK
        doNothing().when(otpServiceRepo).invalidateOtp(otp);

        //TEST
        otpService.invalidateOtp(otp);

        //RESULTS
        Assertions.assertNotNull(otp);
        verify(otpServiceRepo, times(1)).invalidateOtp(otp);
    }
}
