package com.example.autenticationservice.domain.util.jwt;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class AccessTokenJwtTest {

    @InjectMocks
    private AccessTokenJwt accessTokenJwt;

    private String jwtSecret = "testSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecrettestSecret";
    private int jwtAccessExpireMs = 300000;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(accessTokenJwt, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(accessTokenJwt, "jwtAccessExpireMs", jwtAccessExpireMs);
    }

    @Test
    public void shouldGenerateAccessToken_whenAllOk() {
        //PARAMETERS
        String username = "usernameTest";

        //TEST
        String result = accessTokenJwt.generateToken(username);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test //testiamo il metodo validate della classe padre che è astratta
    public void shouldValidateAccessToken_whenAllOk() {
        //PARAMETERS
        String username = "usernameTest";

        //TEST
        String authToken = accessTokenJwt.generateToken(username);
        boolean result = accessTokenJwt.validateToken(authToken);

        //RESULTS
        Assertions.assertNotNull(authToken);
        Assertions.assertTrue(result);
    }

    @Test
    public void shouldReturnFalse_whenInvalidToken() {
        //PARAMETERS
        String invalidToken = "header.payload.signature";

        //TEST
        boolean result = accessTokenJwt.validateToken(invalidToken);

        //RESULTS
        Assertions.assertFalse(result);
    }

    @Test
    public void shouldReturnFalse_whenTokenExpired() {
        //PARAMETERS
        String expiredToken = Jwts.builder()
                .setSubject("usernameTest")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - jwtAccessExpireMs)) //scaduto
                .signWith(accessTokenJwt.key(), SignatureAlgorithm.HS512)
                .compact();

        //TEST + RESULTS
        Assertions.assertThrows(ExpiredJwtException.class, () -> {
            accessTokenJwt.validateToken(expiredToken);
        });
    }

    @Test
    public void shouldReturnFalse_whenUnsupportedToken() {
        //PARAMETERS
        //ps. non avevo idea se non mettere una stringa già fatta
        String unsupportedToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VybmFtZVRlc3QiLCJpYXQiOjE2NTY3MjQ3ODIsImV4cCI6MTY1NjcyNzM4Mn0.Jf6tsoF0bD9F9dXQg3F1Ba6rPfQaRzB3HMQ1dWz5eV5J_1kdygbG9_oLuwVGH2i5_JgV4lppzHbve_Tczoy32a0rVwBq8xMBJQ9pftNK2SOvbmGrZgfYn2sptHfqTxBQTzngtOsRe-Ym1QXqYUNy1l0VrGjdnvnCo1d3RP6OVv2uSceZkv_jFq_s9iKvEvwhzmbpV5AVz3IgYrESzpn_9EHY78V6BfT9jkpgoFdpLCnInLnQ8RxEdH9YphGl4Xh7c8hlw0H50x4nxRJgqYNlMDrbUjtMGRzv-XjRm3w-D0VgWzH76X5xtNxg6eqG6eD0cG23lXyNj9zGg0khojBzVlvXkxMiRQQJxqOAZc_gxya_s7lJrChOqX0HXq-VN2H2gInICVfV8b8w";

        //TEST
        boolean result = accessTokenJwt.validateToken(unsupportedToken);

        //RESULTS
        Assertions.assertFalse(result);
    }

    @Test
    public void shouldReturnFalse_whenJwtClaimIsEmpty() {
        //PARAMETERS
        String emptyClaimToken = "";

        //TEST
        boolean result = accessTokenJwt.validateToken(emptyClaimToken);

        //RESULTS
        Assertions.assertFalse(result);
    }

    @Test
    public void shouldGetUsernameFromToken_whenAllOk() {
        //PARAMETERS
        String token = accessTokenJwt.generateToken("usernameTest");

        //TEST
        String result = accessTokenJwt.getUsernameFromToken(token);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }

}