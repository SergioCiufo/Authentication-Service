package com.example.autenticationservice.domain.service;

import com.example.autenticationservice.domain.model.login.FirstStepRequest;
import com.example.autenticationservice.domain.model.login.FirstStepResponse;
import jakarta.servlet.http.HttpSession;

public interface LoginService {
                                                                        //ho aggiunto la session per il sessionId
    public FirstStepResponse firstStep(FirstStepRequest firstStepRequest, HttpSession session);
}
