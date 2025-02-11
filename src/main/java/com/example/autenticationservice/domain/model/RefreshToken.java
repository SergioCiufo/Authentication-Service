package com.example.autenticationservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data //questo ci crea getter e setter in automatico
@Builder(toBuilder = true) //interfaccia differente e fluida per settare parametri //tobuilder da una classe di creare il builder
@AllArgsConstructor //costruttore con tutti i parametri
@NoArgsConstructor //costruttore vuoto
public class RefreshToken {
    private Integer id;
    private User user;
    private String refreshToken;
    private LocalDateTime createdAt;
    private LocalDateTime expireDate;
    private Boolean valid;
}
