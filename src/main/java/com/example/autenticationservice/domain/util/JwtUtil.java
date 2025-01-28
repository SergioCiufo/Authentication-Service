package com.example.autenticationservice.domain.util;

import com.example.autenticationservice.domain.jwt.AccessToken;
import com.example.autenticationservice.domain.jwt.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JwtUtil {

    private final AccessToken accessToken;
    private final RefreshToken refreshToken;

    //Genera un nuovo JWT per il nome utente specificato
    //Imposta il soggetto (setSubject) con il nome utente
    //Imposta la data di emissione (setIssuedAt) e la data di scadenza (setExpiration)
    //Firma il token utilizzando l'algoritmo HS512 con la chiave segreta
    public String generateAccessToken(String username) {
        return accessToken.generateToken(username);
    }

    //Verifica la validità del token JWT fornito
    //Gestisce eccezioni per token malformati, scaduti, non supportati o con argomenti non validi
    //Restituisce true se il token è valido, altrimenti registra un errore specifico e restituisce false
    public boolean validateAccessToken(String token) {
        return accessToken.validateToken(token);
    }


    //Estrae il nome utente dal JWT fornito
    public String getUsernameFromAccessToken(String token) {
        return accessToken.getUsernameFromToken(token);
    }

    public String getAccessJwtFromHeader(HttpServletRequest request){
        return accessToken.getAccessJwtFromHeader(request);
    }

    public ResponseCookie generateRefreshToken(String username) {
        return refreshToken.generateCookie(username);
    }

    public String getRefreshJwtFromCookie(HttpServletRequest request) {
        return refreshToken.getJwtFromCookie(request);
    }

    //Verifica la validità del token JWT fornito
    //Gestisce eccezioni per token malformati, scaduti, non supportati o con argomenti non validi
    //Restituisce true se il token è valido, altrimenti registra un errore specifico e restituisce false
    public boolean validateRefreshToken(String token) {
        return refreshToken.validateToken(token);
    }

    //Estrae il nome utente dal JWT fornito
    public String getUsernameFromRefreshToken(String token) {
        return refreshToken.getUsernameFromToken(token);
    }

    public ResponseCookie getCleanRefreshTokenCookie() {
       return refreshToken.getCleanJwtCookie();
    }
}
