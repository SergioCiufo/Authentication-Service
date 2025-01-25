package com.example.autenticationservice.domain.jwt;

import com.example.autenticationservice.domain.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;

import java.security.Key;

public abstract class CookieManager {

    private final Logger logger = LoggerFactory.getLogger(CookieManager.class);

    protected String cookieName;
    protected int cookieExpireTime;
    protected String path = "api/";

    //Genera un nuovo JWT per il nome utente specificato
    //Imposta il soggetto (setSubject) con il nome utente
    //Imposta la data di emissione (setIssuedAt) e la data di scadenza (setExpiration)
    //Firma il token utilizzando l'algoritmo HS512 con la chiave segreta
    public abstract String generateToken(String username);

    //Restituisce la chiave segreta utilizzata per firmare e verificare i JWT
    //Utilizza Keys.hmacShaKeyFor per decodificare il segreto base64 configurato
    public abstract Key key();

    //Genera un nuovo JWT per l'utente specificato e lo restituisce come ResponseCookie
    //Imposta il cookie HTTP con le impostazioni necessarie (path, maxAge, httpOnly)
    public abstract ResponseCookie generateCookie(String username);

    //Crea un cookie vuoto per rimuovere il JWT
    //Viene utilizzato quando è necessario eliminare il JWT memorizzato nel client
    public ResponseCookie getCleanJwtCookie(){
        return ResponseCookie.from(cookieName, null)
                .path(path)
                .maxAge(0)
                .build();
    }

    //Verifica la validità del token JWT fornito
    //Gestisce eccezioni per token malformati, scaduti, non supportati o con argomenti non validi
    //Restituisce true se il token è valido, altrimenti registra un errore specifico e restituisce false
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    //Estrae il nome utente dal JWT fornito
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

}
