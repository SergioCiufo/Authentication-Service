package com.example.autenticationservice.domain.service;

import com.example.autenticationservice.domain.model.register.FirstStepRegisterRequest;
import com.example.autenticationservice.domain.model.register.FirstStepRegisterResponse;

public interface RegisterService {
    public FirstStepRegisterResponse firstStep(FirstStepRegisterRequest request);
}
