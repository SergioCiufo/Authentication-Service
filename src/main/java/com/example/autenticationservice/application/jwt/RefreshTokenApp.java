package com.example.autenticationservice.application.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
@RequiredArgsConstructor
public class RefreshTokenApp {

    @Value("${spring.app.jwtRefreshCookieName}")
    private String jwtRefreshCookie;

    @Value("${spring.app.jwtRefreshExpirations}")
    private int jwtRefreshExpireMs;

    private final String path = "api/";

    private final HttpServletRequest request;

    //Recupera il JWT dal cookie HTTP della richiesta HttpServletRequest
    public String getJwtFromCookie() {
        Cookie cookie = WebUtils.getCookie(request, jwtRefreshCookie);
        if (cookie != null) {
            return cookie.getValue();
        }else{
            return null;
        }
    }

    public ResponseCookie generateCookie(String token) {
        return ResponseCookie.from(jwtRefreshCookie, token)
                .path(path)
                .maxAge(jwtRefreshExpireMs/1000) //convertito in secondi
                .httpOnly(true) //evitiamo manimolazioni javascript
                .secure(false) //non usiamo https
                .build();
    }

    //Crea un cookie vuoto per rimuovere il JWT
    //Viene utilizzato quando è necessario eliminare il JWT memorizzato nel client
    public ResponseCookie getCleanJwtCookie(){
        return ResponseCookie.from(jwtRefreshCookie, null)
                .path(path)
                .maxAge(0)
                .build();
    }

}
