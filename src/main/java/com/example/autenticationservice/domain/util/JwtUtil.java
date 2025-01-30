package com.example.autenticationservice.domain.util;

import com.example.autenticationservice.domain.jwt.AccessTokenJwt;
import com.example.autenticationservice.domain.jwt.RefreshTokenJwt;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JwtUtil {

    private final AccessTokenJwt accessTokenJwt;
    private final RefreshTokenJwt refreshTokenJwt;

    //Genera un nuovo JWT per il nome utente specificato
    //Imposta il soggetto (setSubject) con il nome utente
    //Imposta la data di emissione (setIssuedAt) e la data di scadenza (setExpiration)
    //Firma il token utilizzando l'algoritmo HS512 con la chiave segreta
    public String generateAccessToken(String username) {
        return accessTokenJwt.generateToken(username);
    }

    //Verifica la validità del token JWT fornito
    //Gestisce eccezioni per token malformati, scaduti, non supportati o con argomenti non validi
    //Restituisce true se il token è valido, altrimenti registra un errore specifico e restituisce false
    public boolean validateAccessToken(String token) {
        return accessTokenJwt.validateToken(token);
    }


    //Estrae il nome utente dal JWT fornito
    public String getUsernameFromAccessToken(String token) {
        return accessTokenJwt.getUsernameFromToken(token);
    }

    public String getAccessJwtFromHeader(HttpServletRequest request){
        return accessTokenJwt.getAccessJwtFromHeader(request);
    }

    public ResponseCookie generateRefreshToken(String username) {
        return refreshTokenJwt.generateCookie(username);
    }

    public String getRefreshJwtFromCookie(HttpServletRequest request) {
        return refreshTokenJwt.getJwtFromCookie(request);
    }

    //Verifica la validità del token JWT fornito
    //Gestisce eccezioni per token malformati, scaduti, non supportati o con argomenti non validi
    //Restituisce true se il token è valido, altrimenti registra un errore specifico e restituisce false
    public boolean validateRefreshToken(String token) {
        return refreshTokenJwt.validateToken(token);
    }

    //Estrae il nome utente dal JWT fornito
    public String getUsernameFromRefreshToken(String token) {
        return refreshTokenJwt.getUsernameFromToken(token);
    }

    public ResponseCookie getCleanRefreshTokenCookie() {
       return refreshTokenJwt.getCleanJwtCookie();
    }
}
