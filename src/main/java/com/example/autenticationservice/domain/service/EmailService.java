package com.example.autenticationservice.domain.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
