package com.example.autenticationservice.domain.model.verifyOtp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class FirstStepVerifyOtpResponse {
    private String token;
}
