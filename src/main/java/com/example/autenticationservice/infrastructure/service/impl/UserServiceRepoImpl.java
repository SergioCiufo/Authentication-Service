package com.example.autenticationservice.infrastructure.service.impl;

import com.example.autenticationservice.domain.api.UserServiceRepo;
import com.example.autenticationservice.domain.exceptions.CredentialTakenException;
import com.example.autenticationservice.domain.exceptions.InvalidCredentialsException;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.infrastructure.mapper.EntityMappers;
import com.example.autenticationservice.infrastructure.model.UserEntity;
import com.example.autenticationservice.infrastructure.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserServiceRepoImpl implements UserServiceRepo {
    private final UserRepository userRepository;
    private final EntityMappers entityMappers;

//userEntity per staccare?
    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(entityMappers::convertToDomain);
    }

    @Override
    public Optional<User> getUserByUsernameAndPassword(String username, String password) {
        Optional<UserEntity> userEntity = userRepository.findByUsernameAndPassword(username, password);
        if(userEntity.isEmpty()){
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return userEntity.map(entityMappers::convertToDomain);
    }

    @Override
    @Transactional
    public void register(User user) {
        Optional<UserEntity> existingUserByUsername = userRepository.findByUsername(user.getUsername());
        Optional<UserEntity> existingUserByEmail = userRepository.findByEmail(user.getEmail());

        if (existingUserByUsername.isPresent() && existingUserByEmail.isPresent()) {
            throw new CredentialTakenException("Username and email already exists");
        }

        if (existingUserByUsername.isPresent()) {
            throw new CredentialTakenException("Username is already taken");
        }

        if (existingUserByEmail.isPresent()) {
            throw new CredentialTakenException("Email is already taken");
        }

        UserEntity userEntity = entityMappers.convertFromDomain(user);

        userRepository.save(userEntity);

    }

}
