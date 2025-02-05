package com.example.autenticationservice.application.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessTokenApp{

    private final HttpServletRequest request;

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
