package com.example.autenticationservice.domain.util;

import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
public class OtpUtilTest {
    @InjectMocks
    private OtpUtil otpUtil;

    @Test
    public void shouldGenerateOtp_whenAllOk(){
        //PARAMETERS
        User user = User.builder()
                .id(null)
                .name("nameTest")
                .username("usernameTest")
                .email("emailTest")
                .password("pswTest")
                .otpList(null)
                .refreshTokenList(null)
                .build();

        String sessionId = "sessionIdTest";

        //TEST
        Otp result = otpUtil.generateOtp(user, sessionId);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getOtp());
        Assertions.assertEquals(6, result.getOtp().length()); //se l'otp è lungo 6
        Assertions.assertEquals(sessionId, result.getSessionId());
        Assertions.assertTrue(result.getCreatedAt() > 0);
        Assertions.assertTrue(result.getExpiresAt() > result.getCreatedAt());
        Assertions.assertEquals(0, result.getAttempts());
        Assertions.assertTrue(result.isValid());
    }

    @Test
    public void shouldCalculateOtpExpirationTime_whenAllOk() {
        //PARAMETERS
        long currentTime = System.currentTimeMillis();

        //TEST
        long expirationTime = otpUtil.calculateOtpExpirationTime();

        //RESULTS
        Assertions.assertTrue(expirationTime > currentTime); // L'expirationTime deve essere maggiore del currentTime
        Assertions.assertEquals(60 * 1000, expirationTime - currentTime); //la differenza da 1 minuto
    }

    @Test
    public void shouldReturnTrue_whenOtpIsExpired(){
        //PARAMETERS
        long now = System.currentTimeMillis();
        long expiredTime = Instant.now().toEpochMilli() - 1000; //scaduto 1 sec fa
        Otp otp = Otp.builder()
                .expiresAt(expiredTime)
                .build();

        //TEST
        boolean result = otpUtil.isOtpExpired(otp);

        //RESULTS
        Assertions.assertTrue(result);
        Assertions.assertTrue(now > otp.getExpiresAt());
    }

    @Test
    public void shouldReturnFalse_whenOtpIsNotExpired(){
        //PARAMETERS
        long now = System.currentTimeMillis();
        long expiredTime = Instant.now().toEpochMilli() + 10000; //ancora attivo
        Otp otp = Otp.builder()
                .expiresAt(expiredTime)
                .build();

        //TEST
        boolean result = otpUtil.isOtpExpired(otp);

        //RESULTS
        Assertions.assertFalse(result);
        Assertions.assertFalse(now > otp.getExpiresAt());
    }

    @Test
    public void shouldIncreaseOtpAttempts_whenAllOk() {
        //PARAMETERS
        Otp otp = Otp.builder()
                .attempts(0)
                .build();

        int attempt = otp.getAttempts();

        //TEST
        Otp result = otpUtil.increaseOtpAttempt(otp);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(attempt + 1, result.getAttempts());
    }

    @Test
    public void shouldCheckOtpMaxAttemptsTrue_whenOtpIsExpired() {
        //PARAMETERS
        Otp otp = Otp.builder()
                .attempts(2)
                .build();

        int maxAttempts = 3;

        //TEST
        boolean result = otpUtil.checkOtpMaxAttempt(otp);

        //RESULTS
        Assertions.assertTrue(result);
        Assertions.assertEquals(maxAttempts - 1, otp.getAttempts()); //parte da zero
    }

    @Test
    public void shouldCheckOtpMaxAttemptsFalse_whenOtpIsNotExpired() {
        //PARAMETERS
        Otp otp = Otp.builder()
                .attempts(0)
                .build();
        int maxAttempts = 3;

        //TEST
        boolean result = otpUtil.checkOtpMaxAttempt(otp);

        //RESULTS
        Assertions.assertFalse(result);
        Assertions.assertNotEquals(maxAttempts - 1, otp.getAttempts());
    }
}
