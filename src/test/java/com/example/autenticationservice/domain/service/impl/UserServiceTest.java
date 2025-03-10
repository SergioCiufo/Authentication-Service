package com.example.autenticationservice.domain.service.impl;

import com.example.autenticationservice.domain.exceptions.InvalidCredentialsException;
import com.example.autenticationservice.domain.model.User;
import com.example.autenticationservice.domain.repository.UserServiceRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserServiceRepo userServiceRepo;

    @Test
    public void shouldRegisterUser_whenAllOk(){
        //PARAMETERS
        User user = User.builder()
                .id(null)
                .name("nameTest")
                .username("usernameTest")
                .email("emailTest")
                .password("pswTest")
                .otpList(null)
                .refreshTokenList(null)
                .build();

        //MOCK
        doNothing().when(userServiceRepo).register(user);

        //TEST
        userService.register(user);

        //RESULTS
        verify(userServiceRepo, times(1)).register(user);
    }

    @Test
    public void shouldGetUserByUsername_whenAllOk(){
        //PARAMETERS
        String username = "usernameTest";
        User user = User.builder()
                .id(null)
                .name("nameTest")
                .username("usernameTest")
                .email("emailTest")
                .password("pswTest")
                .otpList(null)
                .refreshTokenList(null)
                .build();

        //MOCK
        doReturn(Optional.of(user)).when(userServiceRepo).getUserByUsername(username);

        //TEST
        User result = userService.getUserByUsername(username);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(username, result.getUsername());
        verify(userServiceRepo, times(1)).getUserByUsername(username);
    }

    @Test
    public void shouldThrowInvalidCredentialsException_whenGetUserByUsernameCredentialsNotMatch(){
        //PARAMETERS
        String username = "invalidUsername";

        //MOCK
        doReturn(Optional.empty()).when(userServiceRepo).getUserByUsername(username);

        //TEST + RESULTS
        Assertions.assertThrows(InvalidCredentialsException.class, () -> userService.getUserByUsername(username));
    }

    @Test
    public void shouldGetUserByUsernameAndPassword_whenAllOk(){
        //PARAMETERS
        String username = "usernameTest";
        String password = "pswTest";
        User user = User.builder()
                .id(null)
                .name("nameTest")
                .username("usernameTest")
                .email("emailTest")
                .password("pswTest")
                .otpList(null)
                .refreshTokenList(null)
                .build();

        //MOCK
        doReturn(Optional.of(user)).when(userServiceRepo).getUserByUsernameAndPassword(username, password);

        //TEST
        User result = userService.getUserByUsernameAndPassword(username, password);

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(username, result.getUsername());
        Assertions.assertEquals(password, result.getPassword());
        verify(userServiceRepo, times(1)).getUserByUsernameAndPassword(username, password);
    }

    @Test
    public void shouldThrowInvalidCredentialsException_whenGetUserByUsernameAndPasswordCredentialsNotMatch(){
        //PARAMETERS
        String username = "invalidUsername";
        String password = "invalidPsw";

        //MOCK
        doReturn(Optional.empty()).when(userServiceRepo).getUserByUsernameAndPassword(username, password);

        //TEST + RESULTS
        Assertions.assertThrows(InvalidCredentialsException.class, () -> userService.getUserByUsernameAndPassword(username, password));
    }

    @Test
    public void shouldGetUserList_whenAllOk(){
        //PARAMETERS
        User user1 = User.builder()
                .id(null)
                .name("nameTest")
                .username("usernameTest")
                .email("emailTest")
                .password("pswTest")
                .otpList(null)
                .refreshTokenList(null)
                .build();

        User user2 = User.builder()
                .id(null)
                .name("nameTest")
                .username("usernameTest")
                .email("emailTest")
                .password("pswTest")
                .otpList(null)
                .refreshTokenList(null)
                .build();

        List<User> userList = List.of(user1, user2);

        //MOCK
        doReturn(userList).when(userServiceRepo).getUserList();

        //TEST
        List<User> result = userService.getUserList();

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(user1, result.get(0));
        Assertions.assertEquals(user2, result.get(1));
        verify(userServiceRepo, times(1)).getUserList();
    }

    @Test
    public void shouldReturnEmptyList_whenNoUsersExist(){
        //MOCK
        doReturn(Collections.emptyList()).when(userServiceRepo).getUserList();

        //TEST
        List<User> result = userService.getUserList();

        //RESULTS
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(userServiceRepo, times(1)).getUserList();
    }
}
