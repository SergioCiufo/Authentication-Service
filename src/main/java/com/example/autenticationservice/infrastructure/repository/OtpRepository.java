package com.example.autenticationservice.infrastructure.repository;

import com.example.autenticationservice.domain.model.Otp;
import com.example.autenticationservice.infrastructure.model.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Integer> {
    Optional<OtpEntity> findOtpBySessionIdAndValidTrue(String sessionId);

    @Modifying
    @Query("UPDATE OtpEntity o SET o.valid = false WHERE o.otp = :otp")
    void invalidateOtp(@Param("otp") String otp);

}
