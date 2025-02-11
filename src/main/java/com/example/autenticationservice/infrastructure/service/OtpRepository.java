package com.example.autenticationservice.infrastructure.service;

import com.example.autenticationservice.domain.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Integer> {
    Optional<Otp> findOtpBySessionId(String sessionId);

    Optional<Otp> findOtpBySessionIdAndValidTrue(String sessionId);

    @Modifying
    @Query("UPDATE Otp o SET o.valid = false WHERE o.otp = :otp")
    void invalidateOtp(@Param("otp") String otp);

}
