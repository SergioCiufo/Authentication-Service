package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.exceptions.MissingTokenException;
import com.example.autenticationservice.domain.exceptions.TokenExpiredException;
import com.example.autenticationservice.domain.model.verifyToken.FirstStepVerifyTokenResponse;
import com.example.autenticationservice.domain.service.VerifyTokenService;
import com.example.autenticationservice.domain.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VerifyTokenImpl implements VerifyTokenService {

    private final Logger logger = LoggerFactory.getLogger(VerifyTokenImpl.class);

    private final JwtUtil jwtUtil;

    @Override
    public FirstStepVerifyTokenResponse firstStep(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        //voglio recuperarel 'access token
        String accessToken = jwtUtil.getAccessJwtFromHeader(request);


        if (accessToken == null || accessToken.isEmpty()) {
            logger.error("Access token mancante");
            throw new MissingTokenException("Token mancante o inesistente");
        }

        logger.info("Access token: {}",accessToken);

        try{
            jwtUtil.validateAccessToken(accessToken);
        }catch (ExpiredJwtException e){
            logger.error("Access token scaduto, prova ottenimento nuovo tramite refresh token");
            throw new TokenExpiredException("Access token scaduto, prova ottenimento nuovo tramite refresh token");
        }

        logger.debug("Access token prima di estrarre lo username: {}", accessToken);

        String username = jwtUtil.getUsernameFromAccessToken(accessToken);
        logger.info("Username dall'accessToken: {}",username);

        return FirstStepVerifyTokenResponse.builder()
                .username(username)
                .build();
    }
}