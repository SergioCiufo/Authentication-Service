package com.example.autenticationservice.domain.model.verifyOtp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class FirstStepVerifyOtpRequest {
    private String otp;
    private String sessionOtp; //andrebbe rimosso poiché non è un dato che deve inviare il client
}
