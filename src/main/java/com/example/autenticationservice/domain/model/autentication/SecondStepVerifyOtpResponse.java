package com.example.autenticationservice.domain.model.autentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SecondStepVerifyOtpResponse {
    private String accessToken;
    private String refreshToken;
}
