package com.example.autenticationservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Otp {
    private Integer id;
    private User user;
    private String otp;
    private String sessionId;
    private long createdAt;
    private long expiresAt;
    private Integer attempts;
    private boolean valid;
}
