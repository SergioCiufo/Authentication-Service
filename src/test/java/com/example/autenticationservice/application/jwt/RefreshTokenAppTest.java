package com.example.autenticationservice.application.jwt;

import com.example.autenticationservice.domain.service.AutenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.util.WebUtils;

import static org.mockito.Mockito.*;

//@SpringBootTest(classes = AutenticationService.class) //nel caso volessimo usare application-test.yaml
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class RefreshTokenAppTest {
    @InjectMocks
    private RefreshTokenApp refreshTokenApp;

    @Mock
    private HttpServletRequest request;

    private MockedStatic<WebUtils> mockedWebUtils;

//    @Value("${spring.app.jwtRefreshCookieName}") //nel caso volessimo usare application-test.yaml
//    private String jwtRefreshCookie;
    private String jwtRefreshCookie = "jwtRefreshCookie";
    private int jwtRefreshExpireMs = 1;
    private String path = "api/";

    @BeforeEach
    public void setUp() {
        mockedWebUtils = mockStatic(WebUtils.class);
        ReflectionTestUtils.setField(refreshTokenApp, "request", request);
        ReflectionTestUtils.setField(refreshTokenApp, "jwtRefreshCookie", jwtRefreshCookie);
        ReflectionTestUtils.setField(refreshTokenApp, "jwtRefreshExpireMs", jwtRefreshExpireMs);
        ReflectionTestUtils.setField(refreshTokenApp, "path", path);
    }

    @AfterEach
    public void tearDown() {
        mockedWebUtils.close();
    }


//    @Test
//    public void shouldGetJwtFromCookie_whenAllOk() {
//        //PARAMETERS
//        String token = "token";
//        Cookie cookie = new Cookie(jwtRefreshCookie, token);
//
//        //MOCK
//        mockedWebUtils.when(() -> WebUtils.getCookie(request, jwtRefreshCookie))
//                .thenReturn(cookie);
//
//        //TEST
//        String result = refreshTokenApp.getJwtFromCookie();
//        System.out.println("Returned token: " + result);
//
//        //RESULTS
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(token, result);
//    }
//
//    @Test
//    public void shouldGetJwtFromCookieReturnNull_whenCookieNotExist() {
//        //MOCK
//        mockedWebUtils.when(() -> WebUtils.getCookie(request, jwtRefreshCookie))
//                .thenReturn(null);
//
//        //TEST
//        String result = refreshTokenApp.getJwtFromCookie();
//
//        //RESULTS
//        Assertions.assertNull(result);
//    }
//
//    @Test
//    public void shouldThrowError_whenWebUtilsFails() { //simula un'eccezione quando viene chiamato WebUtils.getCookie
//        //MOCK
//        mockedWebUtils.when(() -> WebUtils.getCookie(request, jwtRefreshCookie))
//                .thenThrow(new RuntimeException("error"));
//
//        //TEST
//        Assertions.assertThrows(RuntimeException.class, () -> {
//            refreshTokenApp.getJwtFromCookie();
//        });
//    }

    @Test
    public void shouldGenerateCookie_whenAllOk() {
        //PARAMETERS
        String token = "testToken";

        //MOCK: non serve la parte di mock
        //perché il metodo generateCookie non dipende da nessuna classe esterna o servizio.

        //TEST
        ResponseCookie cookie = refreshTokenApp.generateCookie(token);

        //RESULTS
        Assertions.assertNotNull(cookie);
        Assertions.assertEquals(jwtRefreshCookie, cookie.getName());
        Assertions.assertEquals(token, cookie.getValue());
        Assertions.assertEquals(path, cookie.getPath());
        Assertions.assertEquals(jwtRefreshExpireMs / 1000, cookie.getMaxAge().getSeconds());
        Assertions.assertTrue(cookie.isHttpOnly());
        Assertions.assertFalse(cookie.isSecure());
    }

    @Test
    void shouldCleanJwtCookie_whenAllOk() {
        //TEST
        ResponseCookie cookie = refreshTokenApp.getCleanJwtCookie();

        //RESULTS
        Assertions.assertNotNull(cookie);
        Assertions.assertEquals(jwtRefreshCookie, cookie.getName());
        Assertions.assertTrue(cookie.getValue().isEmpty());
        Assertions.assertEquals(path, cookie.getPath());
        Assertions.assertEquals(0, cookie.getMaxAge().getSeconds());

    }


}
