package com.example.autenticationservice.domain.util;

import com.example.autenticationservice.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//singleton creata per creare un unica istanza di userList così da verificare registrazione e login etc
//TODO da eliminare una volta introdotto il database

@Component
public class UserListUtil {
    private List<User> userList = new ArrayList<>() {{
        add(new User(1,"admin", "admin", "admin@admin.com", "admin", new ArrayList<>(), null));
        add(new User(2,"name", "username", "email@email.com", "password", new ArrayList<>(), null));
        add(new User(3,"test", "test", "test@test.com", "test", new ArrayList<>(), null));
    }};

    // Ottieni la lista di utenti
    public List<User> getUserList() {
        return userList;
    }
}