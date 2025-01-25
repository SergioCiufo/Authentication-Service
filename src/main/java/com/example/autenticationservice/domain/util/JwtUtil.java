package com.example.autenticationservice.domain.util;

import com.example.autenticationservice.domain.jwt.AccessTokenManager;
import com.example.autenticationservice.domain.jwt.RefreshTokenManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JwtUtil {

    private final AccessTokenManager accessTokenManager;
    private final RefreshTokenManager refreshTokenManager;

    public String getAccessJwtFromCookie(HttpServletRequest request) {
        return accessTokenManager.getJwtFromCookie(request);
    }

    public String getRefreshJwtFromCookie(HttpServletRequest request) {
        return refreshTokenManager.getJwtFromCookie(request);
    }

    //Genera un nuovo JWT per il nome utente specificato
    //Imposta il soggetto (setSubject) con il nome utente
    //Imposta la data di emissione (setIssuedAt) e la data di scadenza (setExpiration)
    //Firma il token utilizzando l'algoritmo HS512 con la chiave segreta
    public ResponseCookie generateAccessToken(String username) {
        return accessTokenManager.generateCookie(username);
    }

    public ResponseCookie generateRefreshToken(String username) {
        return refreshTokenManager.generateCookie(username);
    }


    //Crea un cookie vuoto per rimuovere il JWT
    //Viene utilizzato quando è necessario eliminare il JWT memorizzato nel client
    public ResponseCookie getCleanAccessTokenCookie() {
        return accessTokenManager.getCleanJwtCookie();
    }

    public ResponseCookie getCleanRefreshTokenCookie() {
        return refreshTokenManager.getCleanJwtCookie();
    }

    //Verifica la validità del token JWT fornito
    //Gestisce eccezioni per token malformati, scaduti, non supportati o con argomenti non validi
    //Restituisce true se il token è valido, altrimenti registra un errore specifico e restituisce false
    public boolean validateAccessToken(String token) {
        return accessTokenManager.validateToken(token);
    }

    public boolean validateRefreshToken(String token) {
        return refreshTokenManager.validateToken(token);
    }

    //Estrae il nome utente dal JWT fornito
    public String getUsernameFromAccessToken(String token) {
        return accessTokenManager.getUsernameFromToken(token);
    }

    public String getUsernameFromRefreshToken(String token) {
        return refreshTokenManager.getUsernameFromToken(token);
    }
}
