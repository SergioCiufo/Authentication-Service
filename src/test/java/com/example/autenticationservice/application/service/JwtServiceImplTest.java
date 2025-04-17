package com.example.autenticationservice.application.service;

import com.example.autenticationservice.application.jwt.AccessTokenApp;
import com.example.autenticationservice.application.jwt.RefreshTokenApp;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTest {

    @InjectMocks
    private JwtServiceImpl jwtServiceImpl;

    @Mock
    private AccessTokenApp accessTokenApp;

    @Mock
    private RefreshTokenApp refreshTokenApp;

    @Mock
    HttpServletResponse httpServletResponse;

    @Test
    void shouldExtractAccessToken_whenAllOk() {
        //PARAMETERS
        String expectedToken = "accessToken";

        //MOCK
        doReturn(expectedToken).when(accessTokenApp).getAccessJwtFromHeader();

        //TEST
        String result = jwtServiceImpl.extractAccessJwt();

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedToken, result);
    }

    @Test
    void shouldThrowException_whenExtractAccessTokenFails() {
        //MOCK
        doThrow(RuntimeException.class).when(accessTokenApp).getAccessJwtFromHeader();

        //TEST + RESULT
        Assertions.assertThrows(RuntimeException.class, () -> jwtServiceImpl.extractAccessJwt());

    }

    @Test
    void shouldSetAuthorizzationHeader_whenAllOk() {
        //PARAMETERS
        String expectedToken = "accessToken";

        //MOCK
        doNothing().when(httpServletResponse).setHeader("Authorization", "Bearer " + expectedToken);

        //TEST
        jwtServiceImpl.setAuthorizationHeader(expectedToken);

        //RESULTS
        verify(httpServletResponse).setHeader("Authorization", "Bearer " + expectedToken);
    }

    @Test
    void shouldThrowException_whenSetAuthorizzationHeaderFails() {
        //MOCK
        doThrow(RuntimeException.class).when(httpServletResponse).setHeader(eq("Authorization"), anyString());

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () -> jwtServiceImpl.setAuthorizationHeader(null));
    }

    @Test
    void shouldGenerateRefreshToken_whenAllOk() {
        //PARAMETERS
        String username = "username";
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "refreshToken").build();

        //MOCK
        doReturn(cookie).when(refreshTokenApp).generateCookie(username);

        //TEST
        jwtServiceImpl.generateRefreshCookie(username);

        //RESULTS
        verify(refreshTokenApp).generateCookie(username);
        //verifica che gli argomenti passati al metodo setRefreshTokenCookie siano uguali
        verify(httpServletResponse).addCookie(argThat(argument ->
                argument.getName().equals("refreshToken") && argument.getValue().equals("refreshToken")
        ));
    }

    @Test
    void shouldThrowException_whenGenerateRefreshTokenFails() {
        //MOCK
        doThrow(RuntimeException.class).when(refreshTokenApp).generateCookie(anyString());

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () -> jwtServiceImpl.generateRefreshCookie(null));
    }

    @Test
    void shouldGetJwtCleanCookie_whenAllOk() {
        //PARAMETERS
        ResponseCookie cleanCookie = ResponseCookie.from("refreshToken", "refreshToken").build();

        //MOCK
        doReturn(cleanCookie).when(refreshTokenApp).getCleanJwtCookie();

        //TEST
        ResponseCookie result = jwtServiceImpl.getCleanJwtCookie();

        //RESULTS
        Assertions.assertEquals(cleanCookie, result);
        verify(refreshTokenApp).getCleanJwtCookie();
    }

    @Test
    void shouldThrowException_whenGetJwtCleanCookieFails() {
        //MOCK
        doThrow(RuntimeException.class).when(refreshTokenApp).getCleanJwtCookie();

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () -> jwtServiceImpl.getCleanJwtCookie());
    }

}
