package com.example.autenticationservice.infrastructure.model;

import com.example.autenticationservice.domain.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data //questo ci crea getter e setter in automatico
@Builder(toBuilder = true) //interfaccia differente e fluida per settare parametri //tobuilder da una classe di creare il builder
@AllArgsConstructor //costruttore con tutti i parametri
@NoArgsConstructor //costruttore vuoto
@Entity
@Table(name = "RefreshToken")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;
    private String refreshToken;
    private LocalDateTime createdAt;
    private LocalDateTime expireDate;
    private Boolean valid;
}
