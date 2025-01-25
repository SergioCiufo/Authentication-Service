package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.exceptions.MissingTokenException;
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
        //voglio recuperarel 'access token dal cookie
        String accessToken = jwtUtil.getAccessJwtFromCookie(request);

        if (accessToken == null || accessToken.isEmpty()) {
            logger.error("Access token mancante");
            return null;
            //todo
        }

        logger.info("Access token: {}",accessToken);

        try{
            jwtUtil.validateAccessToken(accessToken);
        }catch (ExpiredJwtException e){
            logger.error("Access token scaduto, prova ottenimento nuovo tramite refresh token");
            response.addHeader("Set-Cookie", jwtUtil.getCleanAccessTokenCookie().toString());
            //todo si schianta, controllare
            String refreshToken = jwtUtil.getRefreshJwtFromCookie(request);

            if (refreshToken == null || refreshToken.isEmpty()) {
                logger.error("Refresh token mancante");
                session.invalidate();
                response.addHeader("Set-Cookie", jwtUtil.getCleanRefreshTokenCookie().toString());
                throw new MissingTokenException("Refresh Token mancante, effettuare login");
            }

            logger.info("Refresh token: {}",refreshToken);

            if (!jwtUtil.validateRefreshToken(refreshToken)) {
                logger.error("Refresh token non valido");
                session.invalidate();
                response.addHeader("Set-Cookie", jwtUtil.getCleanRefreshTokenCookie().toString());
                throw new MissingTokenException("Refresh Token non valido, effettuare login");
            }
            String username = jwtUtil.getUsernameFromRefreshToken(refreshToken);
            ResponseCookie newAccessToken = jwtUtil.generateAccessToken(username);

            response.addCookie(new Cookie(newAccessToken.getName(), newAccessToken.getValue()));

            accessToken = jwtUtil.getAccessJwtFromCookie(request);
        }

        String username = jwtUtil.getUsernameFromAccessToken(accessToken);
        logger.info("Username dall'accessToken: {}",username);

        return FirstStepVerifyTokenResponse.builder()
                .username(username)
                .build();
    }
}
