package com.example.autenticationservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
//@Entity
//@Table(name = "Otp"
public class Otp {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //@ManyToOne
    //@JoinColumn(name = "user_id", referenceColumnName = "id", nullable = false)
    private User user;
    private String otp;
    private String sessionId;
    private long createdAt;
    private long expiresAt;
    private Integer attempts;
    private boolean valid;
}
