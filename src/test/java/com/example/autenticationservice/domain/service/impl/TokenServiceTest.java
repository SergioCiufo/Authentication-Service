package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.exceptions.MissingTokenException;
import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.repository.RefreshTokenServiceRepo;
import com.example.autenticationservice.domain.util.jwt.AccessTokenJwt;
import com.example.autenticationservice.domain.util.jwt.RefreshTokenJwt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private AccessTokenJwt accessTokenJwt;

    @Mock
    private RefreshTokenJwt refreshTokenJwt;

    @Mock
    private RefreshTokenServiceRepo refreshTokenServiceRepo;

    @Test
    public void shouldAddRefreshToken_whenAllOk(){
        //PARAMETERS
        User user = new User();
        RefreshToken refreshToken = RefreshToken.builder()
                .id(null)
                .user(user)
                .refreshToken("tokenTest")
                .createdAt(LocalDateTime.now())
                .expireDate(LocalDateTime.now().plusDays(1))
                .build();

        //MOCK
        doNothing().when(refreshTokenServiceRepo).addRefreshToken(refreshToken);

        //TEST
        tokenService.addRefreshToken(refreshToken);

        //RESULTS
        verify(refreshTokenServiceRepo, times(1)).addRefreshToken(refreshToken);
    }

    @Test
    public void shouldGenerateRefreshToken_whenAllOk(){
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

        String refreshToken = "generateRefreshToken";
        int maxAgeInt = 1000;
        LocalDateTime now = LocalDateTime.now();

        //MOCK
        doReturn(refreshToken).when(refreshTokenJwt).generateToken(user.getUsername());
        doReturn(maxAgeInt).when(refreshTokenJwt).getExpirationDate();

        //TEST
        RefreshToken results = tokenService.generateRefreshToken(user);

        //RESULTS
        Assertions.assertNotNull(results);
        Assertions.assertEquals(refreshToken, results.getRefreshToken());
        Assertions.assertEquals(user, results.getUser());
        verify(refreshTokenJwt, times(1)).generateToken(user.getUsername());
        verify(refreshTokenJwt, times(1)).getExpirationDate();
    }

    @Test
    public void shouldGetRefreshToken_whenAllOk(){
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

        RefreshToken refreshToken = RefreshToken.builder()
                .id(null)
                .user(user)
                .refreshToken("tokenTest")
                .createdAt(LocalDateTime.now())
                .expireDate(LocalDateTime.now().plusDays(1))
                .build();

        String refreshTokenString = "tokenTest";

        //MOCK
        doReturn(Optional.of(refreshToken)).when(refreshTokenServiceRepo).getRefreshToken(refreshTokenString);

        //TEST
        RefreshToken results = tokenService.getRefreshToken(refreshTokenString);

        //RESULTS
        Assertions.assertNotNull(results);
        Assertions.assertEquals(refreshTokenString, results.getRefreshToken());
        Assertions.assertEquals(user, results.getUser());
        verify(refreshTokenServiceRepo, times(1)).getRefreshToken(refreshTokenString);
    }

    @Test
    public void shouldThrowMissionTokenException_whenGetRefreshTokenFails(){
        //PARAMETERS
        String refreshTokenString = "invalidToken";

        //MOCK
        doReturn(Optional.empty()).when(refreshTokenServiceRepo).getRefreshToken(refreshTokenString);

        //TEST + RESULTS
        Assertions.assertThrows(MissingTokenException.class, () -> tokenService.getRefreshToken(refreshTokenString));
    }

    @Test
    public void shouldInvalidateRefreshToken_whenAllOk(){
        //PARAMETERS
        String refreshTokenString = "tokenTest";

        //MOCK
        doNothing().when(refreshTokenServiceRepo).invalidateRefreshToken(refreshTokenString);

        //TEST
        tokenService.invalidateRefreshToken(refreshTokenString);

        //RESULTS
        verify(refreshTokenServiceRepo, times(1)).invalidateRefreshToken(refreshTokenString);
    }

    @Test
    public void shouldValidateRefreshToken_whenAllOk(){
        //PARAMETERS
        String token = "tokenTest";

        //MOCK
        doReturn(true).when(refreshTokenJwt).validateToken(token);

        //TEST
        boolean result = tokenService.validateRefreshToken(token);

        //RESULTS
        Assertions.assertTrue(result);
        verify(refreshTokenJwt, times(1)).validateToken(token);
    }

    @Test
    public void shouldGenerateAccessToken_whenAllOk(){
        //PARAMETERS
        String username = "usernameTest";
        String accessToken = "accessTokenTest";

        //MOCK
        doReturn(accessToken).when(accessTokenJwt).generateToken(username);

        //TEST
        String result = tokenService.generateAccessToken(username);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(accessToken, result);
        verify(accessTokenJwt, times(1)).generateToken(username);
    }

    @Test
    public void shouldGetUsernameFromToken_whenAllOk(){
        //PARAMETERS
        String token = "tokenTest";
        String username = "usernameTest";

        //MOCK
        doReturn(username).when(accessTokenJwt).getUsernameFromToken(token);

        //TEST
        String result = tokenService.getUsernameFromToken(token);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(username, result);
        verify(accessTokenJwt, times(1)).getUsernameFromToken(token);
    }

}
