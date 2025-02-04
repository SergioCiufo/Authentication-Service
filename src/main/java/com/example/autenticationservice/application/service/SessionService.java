package com.example.autenticationservice.application.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //setta le final
public class SessionService {

    private final HttpServletRequest request;

    public String getSessionId() {
        HttpSession session = request.getSession();
        return (session != null) ? session.getId() : null;
    }

    // Invalida la sessione
    public void invalidateSession() {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public void setUsername(String username) {
        HttpSession session = request.getSession();
        session.setAttribute("username", username);
    }

    public String getUsername() {
        HttpSession session = request.getSession();
        return session.getAttribute("username").toString();
    }

    public void removeUsername() {
        HttpSession session = request.getSession();
        session.removeAttribute("username");
    }
}

