package com.example.autenticationservice.domain.model.autentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class FirstStepLoginRequest {
    private String username;
    private String password;
    private String sessionId;
}
