package com.example.autenticationservice.domain.model.autentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SecondStepVerifyOtpRequest {
    private String otp;
    private String sessionId; //andrebbe rimosso poiché non è un dato che deve inviare il client
    private String username;
}
