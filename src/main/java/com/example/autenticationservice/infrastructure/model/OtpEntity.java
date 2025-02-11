package com.example.autenticationservice.infrastructure.model;

import com.example.autenticationservice.domain.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Otp")
public class OtpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;
    private String otp;
    private String sessionId;
    private long createdAt;
    private long expiresAt;
    private Integer attempts;
    private boolean valid;
}
