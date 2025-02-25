package com.example.autenticationservice.infrastructure.service.impl;

import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.infrastructure.repository.OtpRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.  *;

@ExtendWith(MockitoExtension.class)
public class OtpServiceImplTest {

    @InjectMocks
    private OtpServiceImpl otpServiceImpl;

    @Mock
    private OtpRepository otpRepository;

    @Test
    void shouldSaveOtp_whenAllOk() {
        //PARAMETERS
        Otp otp = new Otp();
        otp.setOtp("123456");

        //MOCK
        doReturn(otp).when(otpRepository).save(otp);

        //TEST
        otpServiceImpl.saveOtp(otp);

        //RESULT
        Assertions.assertNotNull(otp.getOtp());
        verify(otpRepository, times(1)).save(otp);
    }

    @Test
    void shouldUpdateOtp_whenAllOk() {
        //PARAMETERS
        Otp otp = new Otp();
        otp.setOtp("123456");

        //MOCK
        doReturn(otp).when(otpRepository).save(otp);

        //TEST
        otpServiceImpl.updateOtp(otp);

        //RESULT
        Assertions.assertNotNull(otp.getOtp());
        verify(otpRepository, times(1)).save(otp);
    }

    @Test
    void shouldThrowException_whenSaveOtpFails() { //Simula un errore del database
        //PARAMETERS
        Otp otp = new Otp();
        otp.setOtp("123456");

        //MOCK
        doThrow(new RuntimeException("DB error")).when(otpRepository).save(otp);

        //TEST + RESULT
        Assertions.assertThrows(RuntimeException.class, () -> otpServiceImpl.saveOtp(otp));

        verify(otpRepository, times(1)).save(otp);
    }

    @Test
    void shouldGetValidOtpBySessionId_whenAllOk() {
        //PARAMETERS
        String sessionId = "123456";
        Otp otp = new Otp();

        //MOCK
        doReturn(Optional.of(otp)).when(otpRepository).findOtpBySessionIdAndValidTrue(sessionId);

        //TEST
        Optional<Otp> foundOtp = otpServiceImpl.getValidOtpBySessionId(sessionId);

        //RESULT
        Assertions.assertTrue(foundOtp.isPresent());
        Assertions.assertEquals(otp, foundOtp.get());
        verify(otpRepository, times(1)).findOtpBySessionIdAndValidTrue(sessionId);
    }

    @Test
    void shouldReturnEmptyOptional_whenNoOtpFound() {
        //PARAMETERS
        String sessionId = "123456";
        Otp otp = new Otp();

        //MOCK
        doReturn(Optional.empty()).when(otpRepository).findOtpBySessionIdAndValidTrue(sessionId);

        //TEST
        Optional<Otp> foundOtp = otpServiceImpl.getValidOtpBySessionId(sessionId);

        //RESULT
        Assertions.assertFalse(foundOtp.isPresent());
        verify(otpRepository, times(1)).findOtpBySessionIdAndValidTrue(sessionId);
    }

    @Test
    void shouldThrowException_whenGetValidOtpFails() { //Simula un errore del database
        //PARAMETERS
        String sessionId = "123456";

        //MOCK
        doThrow(new RuntimeException("DB error")).when(otpRepository).findOtpBySessionIdAndValidTrue(sessionId);

        //TEST + RESULT
        Assertions.assertThrows(RuntimeException.class, () -> otpServiceImpl.getValidOtpBySessionId(sessionId));

        verify(otpRepository, times(1)).findOtpBySessionIdAndValidTrue(sessionId);
    }

    @Test
    void shouldInvalidateOtp_whenAllOk() {
        //PARAMETERS
        Otp otp = new Otp();
        otp.setOtp("123456");

        //MOCK
        doNothing().when(otpRepository).invalidateOtp(otp.getOtp());

        //TEST
        otpServiceImpl.invalidateOtp(otp);

        //RESULTS
        verify(otpRepository, times(1)).invalidateOtp(otp.getOtp());

    }

    @Test
    void shouldNotInvalidateOtp_whenOtpIsNull() { //verifica che non venga chiamato il metodo del invalidateOpt
        //TEST + RESULT
        Assertions.assertThrows(NullPointerException.class, () -> otpServiceImpl.invalidateOtp(null));

        verify(otpRepository, times(0)).invalidateOtp(any());
    }
}
