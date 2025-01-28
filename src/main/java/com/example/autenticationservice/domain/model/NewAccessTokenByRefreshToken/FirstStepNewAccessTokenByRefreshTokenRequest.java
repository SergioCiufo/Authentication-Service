package com.example.autenticationservice.domain.model.NewAccessTokenByRefreshToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class FirstStepNewAccessTokenByRefreshTokenRequest {
    private String refreshToken;
}
