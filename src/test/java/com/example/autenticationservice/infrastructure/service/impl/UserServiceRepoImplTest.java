package com.example.autenticationservice.infrastructure.service.impl;

import com.example.autenticationservice.domain.exceptions.CredentialTakenException;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceRepoImplTest {

    @InjectMocks
    private UserServiceRepoImpl userServiceRepoImpl;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldGetUserByUsername_whenIsAllOk(){
        //PARAMETERS
        User user = new User();
        user.setUsername("testuser");

        //MOCK
        doReturn(Optional.of(user)).when(userRepository).findByUsername("testuser");

        //TEST
        Optional<User> foundUser = userServiceRepoImpl.getUserByUsername(user.getUsername());

        //RESULTS
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals("testuser", foundUser.get().getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void shouldReturnEmptyOptional_whenUserNotFound(){
        //PARAMETERS
        User user = new User();
        user.setUsername("testuser");

        //MOCK
        doReturn(Optional.empty()).when(userRepository).findByUsername("testuser");

        //TEST
        Optional<User> foundUser = userServiceRepoImpl.getUserByUsername(user.getUsername());

        //RESULTS
        Assertions.assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void shouldThrowException_whenFailsToFindUserByUsername() { //simula un errore del database
        //MOCK
        when(userRepository.findByUsername("testuser")).thenThrow(new RuntimeException("DB error"));

        //TEST + RESULT
        Assertions.assertThrows(RuntimeException.class, () -> userServiceRepoImpl.getUserByUsername("testuser"));

        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void shouldGetUserByUsernameAndPassword_whenIsAllOk(){
        //PARAMETERS
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        //MOCK
        doReturn(Optional.of(user)).when(userRepository).findByUsernameAndPassword("testuser", "password");

        //TEST
        Optional<User> foundUser = userServiceRepoImpl.getUserByUsernameAndPassword("testuser", "password");

        //RESULTS
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals("testuser", foundUser.get().getUsername());
        Assertions.assertEquals("password", foundUser.get().getPassword());
        verify(userRepository, times(1)).findByUsernameAndPassword("testuser", "password");
    }

    @Test
    void shouldReturnEmptyOptional_whenUserNotFoundAndPassword(){
        //PARAMETERS
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        //MOCK
        doReturn(Optional.empty()).when(userRepository).findByUsernameAndPassword("testuser", "password");

        //TEST
        Optional<User> foundUser = userServiceRepoImpl.getUserByUsernameAndPassword("testuser", "password");

        //RESULTS
        Assertions.assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findByUsernameAndPassword("testuser", "password");
    }

    @Test
    void shouldThrowException_whenFailsToFindUserByUsernameAndPassword() { //simula un errore del database
        //MOCK
        when(userRepository.findByUsernameAndPassword("testuser", "password")).thenThrow(new RuntimeException("DB error"));

        //TEST + RESULT
        Assertions.assertThrows(RuntimeException.class, () -> userServiceRepoImpl.getUserByUsernameAndPassword("testuser", "password"));

        verify(userRepository, times(1)).findByUsernameAndPassword("testuser", "password");
    }

    @Test
    void shouldReturnListOfUsers_whenIsAllOk(){
        //PARAMETERS
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setUsername("testuser");
        userList.add(user);

        //MOCK
        doReturn(List.of(user)).when(userRepository).findAll();

        //TEST
        List<User> userListTest = userServiceRepoImpl.getUserList();

        //RESULT
        Assertions.assertNotNull(userListTest);
        Assertions.assertEquals(userList.size(), userListTest.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyList_whenUserNotFound(){
        //PARAMETERS
        List<User> userList = new ArrayList<>();

        //MOCK
        doReturn(Collections.emptyList()).when(userRepository).findAll();

        //TEST
        List<User> userListTest = userServiceRepoImpl.getUserList();

        //RESULTS
        Assertions.assertNotNull(userListTest);
        Assertions.assertEquals(userList.size(), userListTest.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldThrowException_whenFailsToFindAllUsers() { //simula un errore del database
        //MOCK
        when(userRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        // TEST + RESULT
        Assertions.assertThrows(RuntimeException.class, () -> userServiceRepoImpl.getUserList());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldRegisterUser_whenIsAllOk(){
        //PARAMETERS
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testemail");

        //MOCK
        doReturn(Optional.empty()).when(userRepository).findByUsername("testuser");
        doReturn(Optional.empty()).when(userRepository).findByEmail("testemail");
//        doNothing().when(userRepository).save(user); non va bene
        doReturn(null).when(userRepository).save(user);

        //TEST
        userServiceRepoImpl.register(user);

        //RESULTS
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userRepository, times(1)).findByEmail("testemail");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowException_whenUserameAndEmailExist(){
        //PARAMETERS
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testemail");

        //MOCK
        doReturn(Optional.of(user)).when(userRepository).findByUsername("testuser");
        doReturn(Optional.of(user)).when(userRepository).findByEmail("testemail");

        //TEST
        Assertions.assertThrows(CredentialTakenException.class, () -> {
            userServiceRepoImpl.register(user);
        });

        //VERIFY
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userRepository, times(1)).findByEmail("testemail");
        verify(userRepository, never()).save(user);
    }

    @Test
    void shouldThrowException_whenUsernameExist(){
        //PARAMETERS
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testemail");

        //MOCK
        doReturn(Optional.of(user)).when(userRepository).findByUsername("testuser");
        doReturn(Optional.empty()).when(userRepository).findByEmail("testemail");

        //TEST
        Assertions.assertThrows(CredentialTakenException.class, () -> {
            userServiceRepoImpl.register(user);
        });

        //VERIFY
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userRepository, times(1)).findByEmail("testemail");
        verify(userRepository, never()).save(user);
    }

    @Test
    void shouldThrowException_whenEmailExist(){
        //PARAMETERS
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testemail");

        //MOCK
        doReturn(Optional.empty()).when(userRepository).findByUsername("testuser");
        doReturn(Optional.of(user)).when(userRepository).findByEmail("testemail");

        //TEST
        Assertions.assertThrows(CredentialTakenException.class, () -> {
            userServiceRepoImpl.register(user);
        });

        //VERIFY
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userRepository, times(1)).findByEmail("testemail");
        verify(userRepository, never()).save(user);
    }

    @Test
    void shouldThrowException_whenFailsToSaveUser() {
        //PARAMETERS
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testemail");

        //MOCK
        doReturn(Optional.empty()).when(userRepository).findByUsername("testuser");
        doReturn(Optional.empty()).when(userRepository).findByEmail("testemail");
        doThrow(new RuntimeException("DB error")).when(userRepository).save(user);

        //TEST + RESULT
        Assertions.assertThrows(RuntimeException.class, () -> userServiceRepoImpl.register(user));

        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userRepository, times(1)).findByEmail("testemail");
        verify(userRepository, times(1)).save(user);
    }
}
