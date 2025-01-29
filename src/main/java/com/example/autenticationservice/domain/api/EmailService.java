package com.example.autenticationservice.domain.api;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
