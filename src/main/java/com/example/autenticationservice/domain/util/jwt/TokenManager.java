package com.example.autenticationservice.domain.util.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;

// TODO carino, ma è più una utility, sposta in utils e fai un sottopackage jwt
// ricordami di farti vedere la factory dopo così possiamo farci un fiocchetto su questa cosa

//TODO DONE
public abstract class TokenManager {
    private final Logger logger = LoggerFactory.getLogger(TokenManager.class);

    protected String path = "api/";

    //Genera un nuovo JWT per il nome utente specificato
    //Imposta il soggetto (setSubject) con il nome utente
    //Imposta la data di emissione (setIssuedAt) e la data di scadenza (setExpiration)
    //Firma il token utilizzando l'algoritmo HS512 con la chiave segreta
    public abstract String generateToken(String username);

    //Restituisce la chiave segreta utilizzata per firmare e verificare i JWT
    //Utilizza Keys.hmacShaKeyFor per decodificare il segreto base64 configurato
    public abstract Key key();

    //Verifica la validità del token JWT fornito
    //Gestisce eccezioni per token malformati, scaduti, non supportati o con argomenti non validi
    //Restituisce true se il token è valido, altrimenti registra un errore specifico e restituisce false
    public boolean validateToken(String authToken) throws ExpiredJwtException { //facciamo salire l'ExpiredJwtException per lavorarci
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
            throw e;
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
