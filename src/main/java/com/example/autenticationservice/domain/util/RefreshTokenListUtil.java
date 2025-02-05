package com.example.autenticationservice.domain.util;

import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.domain.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class RefreshTokenListUtil {
    private List<RefreshToken> refreshTokenList = new ArrayList<>() {{
        // Creiamo utenti finti con dati reali
        User admin = new User(1, "admin", "admin", "admin@admin.com", "admin", null, null);
        User name = new User(2, "name", "username", "email@email.com", "password", null, null);
        User test = new User(3, "test", "test", "test@test.com", "test", null, null);

        add(new RefreshToken(1, admin, "12345", LocalDateTime.now(), LocalDateTime.now().plusDays(1), true));
        add(new RefreshToken(2, name, "45678", LocalDateTime.now(), LocalDateTime.now().plusDays(1), true));
        add(new RefreshToken(3, test, "01234", LocalDateTime.now(), LocalDateTime.now().plusDays(1), true));
    }};

    // Ottieni la lista di utenti
    public List<RefreshToken> getRefreshTokenList() {
        return refreshTokenList;
    }

    public void add(RefreshToken refreshToken) {
        refreshTokenList.add(refreshToken);
    }

    public RefreshToken getRefreshToken(String refreshTokenString) {
        List<RefreshToken> refreshTokenList = getRefreshTokenList();

        return refreshTokenList.stream()
                .filter(refToken -> refToken.getRefreshToken().equals(refreshTokenString))
                .findFirst()
                .orElse(null);
    }

    public void invalidate(String refreshToken) {
        RefreshToken refreshTokeToInvalidate = getRefreshToken(refreshToken);
        if(refreshTokeToInvalidate != null) {
            refreshTokeToInvalidate.setValid(false);
        }
    }
}
