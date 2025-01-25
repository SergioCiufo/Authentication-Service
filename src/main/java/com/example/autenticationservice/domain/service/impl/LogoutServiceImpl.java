package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.model.logout.FirstStepLogoutResponse;
import com.example.autenticationservice.domain.service.LogoutService;
import com.example.autenticationservice.domain.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LogoutServiceImpl implements LogoutService {
    private final Logger logger = LoggerFactory.getLogger(LogoutServiceImpl.class);

    private final JwtUtil jwtUtil;

    @Override
    public FirstStepLogoutResponse firstStep(HttpSession session, HttpServletResponse response) {

        //sostiuisce il token con un token "con scadenza immediata, rimuovendolo
        response.addHeader("Set-Cookie", jwtUtil.getCleanAccessTokenCookie().toString());
        response.addHeader("Set-Cookie", jwtUtil.getCleanRefreshTokenCookie().toString());

        session.invalidate();

        logger.info("Logged out successfully");
        return FirstStepLogoutResponse.builder()
                .message("Logout effettuato con successo. Token invalidati.")
                .build();
    }

}
