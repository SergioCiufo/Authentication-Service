package com.example.autenticationservice.infrastructure.service;

import com.example.autenticationservice.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
