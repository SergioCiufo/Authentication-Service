package com.example.autenticationservice.domain.model.autentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SecondStepLoginRequest {
    private String otp;
    private String sessionId;
    private String username;
}
