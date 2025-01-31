package com.example.autenticationservice.domain.model.newAccessTokenByRefreshToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class FirstStepNewAccessTokenByRefreshTokenResponse {
    private String accessToken;
    private String message;
}
