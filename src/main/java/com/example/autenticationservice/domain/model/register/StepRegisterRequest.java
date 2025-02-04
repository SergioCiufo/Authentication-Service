package com.example.autenticationservice.domain.model.register;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class StepRegisterRequest {
    private String name;
    private String username;
    private String email;
    private String password;
}
