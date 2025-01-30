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
//@Entity
//@Table(name = "User")
public class User {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String username;
    private String email;
    private String password;
    //@OneToMany(mappedBy = "user"", cascade = CascadeType.All)
    private List<Otp> otpList;
    //@OneToOne(mappedBy = "user", cascade = CascadeType.All)
    private RefreshToken refreshToken;
}
