package com.example.autenticationservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data //questo ci crea getter e setter in automatico
@Builder(toBuilder = true) //interfaccia differente e fluida per settare parametri //tobuilder da una classe di creare il builder
@AllArgsConstructor //costruttore con tutti i parametri
@NoArgsConstructor //costruttore vuoto

public class User {
    private Integer id;
    private String name;
    private String username;
    private String email;
    private String password;
    private List<Otp> otpList;
    private List<RefreshToken> refreshTokenList;
}
