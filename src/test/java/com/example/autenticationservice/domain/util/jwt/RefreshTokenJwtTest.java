package com.example.autenticationservice.domain.util.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenJwtTest {
    @InjectMocks
    private RefreshTokenJwt refreshTokenJwt;

    private String jwtSecret = "testSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecret";
    private int jwtRefreshExpireMs = 300000;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(refreshTokenJwt, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(refreshTokenJwt, "jwtRefreshExpireMs", jwtRefreshExpireMs);
    }

    @Test
    public void shouldGenerateRefreshToken_whenAllOk(){
        //PARAMETERS
        String username = "usernameTest";

        //TEST
        String result = refreshTokenJwt.generateToken(username);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void shouldGetExpirationDate_whenAllOk(){
        //PARAMETERS
        int expiresIn = jwtRefreshExpireMs;

        //TEST
        int result = refreshTokenJwt.getExpirationDate();

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expiresIn, result);
    }

    @Test //testiamo il metodo validate della classe padre che è astratta
    public void shouldValidateAccessToken_whenAllOk() {
        //PARAMETERS
        String username = "usernameTest";

        //TEST
        String authToken = refreshTokenJwt.generateToken(username);
        boolean result = refreshTokenJwt.validateToken(authToken);

        //RESULTS
        Assertions.assertNotNull(authToken);
        Assertions.assertTrue(result);
    }

    @Test
    public void shouldReturnFalse_whenInvalidToken() {
        //PARAMETERS
        String invalidToken = "header.payload.signature";

        //TEST
        boolean result = refreshTokenJwt.validateToken(invalidToken);

        //RESULTS
        Assertions.assertFalse(result);
    }

    @Test
    public void shouldReturnFalse_whenTokenExpired() {
        //PARAMETERS
        String expiredToken = Jwts.builder()
                .setSubject("usernameTest")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - jwtRefreshExpireMs)) //scaduto
                .signWith(refreshTokenJwt.key(), SignatureAlgorithm.HS512)
                .compact();

        //TEST + RESULTS
        Assertions.assertThrows(ExpiredJwtException.class, () -> {
            refreshTokenJwt.validateToken(expiredToken);
        });
    }

    @Test
    public void shouldReturnFalse_whenUnsupportedToken() {
        //PARAMETERS
        //ps. non avevo idea se non mettere una stringa già fatta
        String unsupportedToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VybmFtZVRlc3QiLCJpYXQiOjE2NTY3MjQ3ODIsImV4cCI6MTY1NjcyNzM4Mn0.Jf6tsoF0bD9F9dXQg3F1Ba6rPfQaRzB3HMQ1dWz5eV5J_1kdygbG9_oLuwVGH2i5_JgV4lppzHbve_Tczoy32a0rVwBq8xMBJQ9pftNK2SOvbmGrZgfYn2sptHfqTxBQTzngtOsRe-Ym1QXqYUNy1l0VrGjdnvnCo1d3RP6OVv2uSceZkv_jFq_s9iKvEvwhzmbpV5AVz3IgYrESzpn_9EHY78V6BfT9jkpgoFdpLCnInLnQ8RxEdH9YphGl4Xh7c8hlw0H50x4nxRJgqYNlMDrbUjtMGRzv-XjRm3w-D0VgWzH76X5xtNxg6eqG6eD0cG23lXyNj9zGg0khojBzVlvXkxMiRQQJxqOAZc_gxya_s7lJrChOqX0HXq-VN2H2gInICVfV8b8w";

        //TEST
        boolean result = refreshTokenJwt.validateToken(unsupportedToken);

        //RESULTS
        Assertions.assertFalse(result);
    }

    @Test
    public void shouldReturnFalse_whenJwtClaimIsEmpty() {
        //PARAMETERS
        String emptyClaimToken = "";

        //TEST
        boolean result = refreshTokenJwt.validateToken(emptyClaimToken);

        //RESULTS
        Assertions.assertFalse(result);
    }

    @Test
    public void shouldGetUsernameFromToken_whenAllOk() {
        //PARAMETERS
        String token = refreshTokenJwt.generateToken("usernameTest");

        //TEST
        String result = refreshTokenJwt.getUsernameFromToken(token);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }


}
