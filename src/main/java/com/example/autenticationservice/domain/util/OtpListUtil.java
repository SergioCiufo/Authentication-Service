package com.example.autenticationservice.domain.util;

import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OtpListUtil {

    private List<Otp> otpList = new ArrayList<>() {{
        // Creiamo utenti finti con dati reali
        User admin = new User(1, "admin", "admin", "admin@admin.com", "admin", new ArrayList<>(), null);
        User name = new User(2, "name", "username", "email@email.com", "password", new ArrayList<>(), null);
        User test = new User(3, "test", "test", "test@test.com", "test", new ArrayList<>(), null);

        add(new Otp(1,admin, "12345", "macarena", 10L, 12L, 3, false));
        add(new Otp(2,name, "45678", "cipresso", 10L, 12L,3, false));
        add(new Otp(3,test, "01234", "test", 10L, 12L,3, false));
    }};

    // Ottieni la lista di utenti
    public List<Otp> getOtpList() {
        return otpList;
    }
}
