package com.example.autenticationservice.application.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccessTokenAppTest {

    @InjectMocks
    private AccessTokenApp accessTokenApp;

    @Mock
    private HttpServletRequest request;

    @Test
    void shouldGetAccessTokenFromHeader_whenAllOk(){
        //PARAMETERS
        String token = "testToken";
        String authorizationHeader = "Bearer "+token;

        //MOCK
        doReturn(authorizationHeader).when(request).getHeader("Authorization");

        //TEST
        String result = accessTokenApp.getAccessJwtFromHeader();

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertTrue(authorizationHeader.startsWith("Bearer "));
        Assertions.assertEquals(token, result);
    }

    @Test
    void shouldNotGetAccessTokenFromHeader_whenNoHeader(){
        //MOCK
        doReturn(null).when(request).getHeader("Authorization");

        //TEST
        String result = accessTokenApp.getAccessJwtFromHeader();

        //RESULTS
        Assertions.assertNull(result);
    }

    @Test
    void shouldNotGetAccessTokenFromHeader_whenInvalidHeader(){
        //PARAMETERS
        String invalidHeader = "invalidHeader";
        doReturn(invalidHeader).when(request).getHeader("Authorization");
        String result = accessTokenApp.getAccessJwtFromHeader();
        Assertions.assertNull(result);
        Assertions.assertNotNull(invalidHeader);
        Assertions.assertFalse(invalidHeader.startsWith("Bearer "));
    }

    @Test
    void shouldThrowRuntimeException_whenUnexpectedError(){
        //MOCK
        doThrow(RuntimeException.class).when(request).getHeader("Authorization");

        //TEST + RESULT
        Assertions.assertThrows(RuntimeException.class, () -> accessTokenApp.getAccessJwtFromHeader());
    }
}
