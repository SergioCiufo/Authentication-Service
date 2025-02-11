package com.example.autenticationservice.infrastructure.repository;

import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.infrastructure.model.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

    @Modifying
    @Query("UPDATE RefreshTokenEntity r SET r.valid = false WHERE r.refreshToken = :refreshToken")
    void invalidateRefreshToken(@Param("refreshToken") String refreshToken);
}
