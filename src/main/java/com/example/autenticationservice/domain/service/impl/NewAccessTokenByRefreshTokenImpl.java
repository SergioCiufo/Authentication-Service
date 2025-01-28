package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.exceptions.MissingTokenException;
import com.example.autenticationservice.domain.model.NewAccessTokenByRefreshToken.FirstStepNewAccessTokenByRefreshTokenRequest;
import com.example.autenticationservice.domain.model.NewAccessTokenByRefreshToken.FirstStepNewAccessTokenByRefreshTokenResponse;
import com.example.autenticationservice.domain.service.NewAccessTokenByRefreshTokenService;
import com.example.autenticationservice.domain.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NewAccessTokenByRefreshTokenImpl implements NewAccessTokenByRefreshTokenService {

    private final Logger logger = LoggerFactory.getLogger(NewAccessTokenByRefreshTokenImpl.class);

    private final JwtUtil jwtUtil;

    @Override
    public FirstStepNewAccessTokenByRefreshTokenResponse firstStep(FirstStepNewAccessTokenByRefreshTokenRequest firstStepRequest, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        String refreshToken = jwtUtil.getRefreshJwtFromCookie(request);

        if (refreshToken == null || refreshToken.isEmpty()) {
            logger.error("Refresh token mancante");
            session.invalidate();
            response.setHeader("Set-Cookie", jwtUtil.getCleanRefreshTokenCookie().toString());
            throw new MissingTokenException("Refresh Token mancante, effettuare login");
        }

        logger.info("Refresh token: {}",refreshToken);

        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            logger.error("Refresh token non valido");
            session.invalidate();
            response.setHeader("Set-Cookie", jwtUtil.getCleanRefreshTokenCookie().toString());
            throw new MissingTokenException("Refresh Token non valido, effettuare login");
        }

        String username = jwtUtil.getUsernameFromRefreshToken(refreshToken);

        String accessToken = jwtUtil.generateAccessToken(username);
        response.setHeader("Authorization", "Bearer " + accessToken);
        logger.info(String.format("Access Token: %s",accessToken));


        return FirstStepNewAccessTokenByRefreshTokenResponse.builder()
                .message("Access Token Rigenerato")
                .accessToken(accessToken)
                .build();
    }
}
