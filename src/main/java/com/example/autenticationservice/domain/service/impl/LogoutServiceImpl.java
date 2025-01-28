package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.model.logout.FirstStepLogoutResponse;
import com.example.autenticationservice.domain.service.LogoutService;
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
public class LogoutServiceImpl implements LogoutService {
    private final Logger logger = LoggerFactory.getLogger(LogoutServiceImpl.class);

    private JwtUtil jwtUtil;


    @Override
    public FirstStepLogoutResponse firstStep(HttpSession session, HttpServletResponse response, HttpServletRequest request) {

        //sostiuisce il token con un token "con scadenza immediata, rimuovendolo
        response.setHeader("Set-Cookie", jwtUtil.getCleanRefreshTokenCookie().toString());

        //prendi ed invalida l'access token
        response.setHeader("Authorization", "Bearer " + null);

        //invalidazione della sessione :()
        session.invalidate();

        logger.info("Logged out successfully");
        return FirstStepLogoutResponse.builder()
                .message("Logout effettuato con successo. Token invalidati.")
                .build();
    }

}
