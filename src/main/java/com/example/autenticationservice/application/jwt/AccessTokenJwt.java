package com.example.autenticationservice.application.jwt;

import java.security.Key;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class AccessTokenJwt extends TokenManager {

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtAccessExpirations}")
    private int jwtAccessExpireMs;

    private final HttpServletRequest request;

    //Genera un nuovo JWT per il nome utente specificato
    //Imposta il soggetto (setSubject) con il nome utente
    //Imposta la data di emissione (setIssuedAt) e la data di scadenza (setExpiration)
    //Firma il token utilizzando l'algoritmo HS512 con la chiave segreta
    @Override
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtAccessExpireMs))
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }

    //Restituisce la chiave segreta utilizzata per firmare e verificare i JWT
    //Utilizza Keys.hmacShaKeyFor per decodificare il segreto base64 configurato
    @Override
    public Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getAccessJwtFromHeader() {
        //Recupera il valore dell'header Authorization
        String authorizationHeader = request.getHeader("Authorization");

        //Controlla se l'header è presente e inizia con "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            //rimuove la parola bearer restituisce solo il token
            return authorizationHeader.substring(7); //7 caratteri di bearer
        }

        return null;
    }

}
