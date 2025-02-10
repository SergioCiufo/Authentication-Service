package com.example.autenticationservice.infrastructure.service;

import com.example.autenticationservice.domain.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    @Modifying
    @Query("UPDATE RefreshToken r SET r.valid = false WHERE r.refreshToken = :refreshToken")
    void invalidateRefreshToken(@Param("refreshToken") String refreshToken);
}
