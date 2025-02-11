package com.example.autenticationservice.infrastructure.repository;

import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.infrastructure.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsernameAndPassword(String username, String password);
}
