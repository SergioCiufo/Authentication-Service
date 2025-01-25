package com.example.autenticationservice.domain.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;

@Component
public class RefreshTokenManager extends TokenCookieManager {
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtRefreshCookieName}")
    private String jwtRefreshCookie;

    @Value("${spring.app.jwtRefreshExpirations}")
    private int jwtRefreshExpireMs;



    //Recupera il JWT dal cookie HTTP della richiesta HttpServletRequest
    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtRefreshCookie);
        if (cookie != null) {
            return cookie.getValue();
        }else{
            return null;
        }
    }

    //Genera un nuovo JWT per il nome utente specificato
    //Imposta il soggetto (setSubject) con il nome utente
    //Imposta la data di emissione (setIssuedAt) e la data di scadenza (setExpiration)
    //Firma il token utilizzando l'algoritmo HS512 con la chiave segreta
    @Override
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpireMs))
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }

    //Restituisce la chiave segreta utilizzata per firmare e verificare i JWT
    //Utilizza Keys.hmacShaKeyFor per decodificare il segreto base64 configurato
    @Override
    public Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    @Override
    public ResponseCookie generateCookie(String username) {
        String token = generateToken(username);
        return ResponseCookie.from(jwtRefreshCookie, token)
                .path(path)
                .maxAge(jwtRefreshExpireMs/1000) //convertito in secondi
                .httpOnly(true)
                .secure(false) //non usiamo https
                .build();
    }

    @Override
    public ResponseCookie getCleanJwtCookie(){
        return ResponseCookie.from(jwtRefreshCookie, null)
                .path(path)
                .maxAge(0)
                .build();
    }
}
