package com.example.autenticationservice.domain.model.verifyToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SecondStepGetAccessTokenByRefreshTokenRequest {
    private String refreshToken;
}
