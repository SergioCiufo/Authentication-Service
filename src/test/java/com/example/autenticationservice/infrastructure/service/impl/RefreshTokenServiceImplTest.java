package com.example.autenticationservice.infrastructure.service.impl;

import com.example.autenticationservice.domain.model.RefreshToken;
import com.example.autenticationservice.infrastructure.repository.RefreshTokenRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceImplTest {
    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenServiceImpl;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void shouldSaveRefreshToken_whenAllOk() {
        //PARAMETERS
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken("refreshToken");

        //MOCKS
        doReturn(refreshToken).when(refreshTokenRepository).save(refreshToken);

        //TEST
        refreshTokenServiceImpl.addRefreshToken(refreshToken);

        //RESULTS
        Assertions.assertNotNull(refreshToken);
        verify(refreshTokenRepository, times(1)).save(refreshToken);
    }

    @Test
    void shouldThrowException_whenSaveRefreshTokenFails() {
        //PARAMETERS
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken("refreshToken");

        //MOCK
        doThrow(RuntimeException.class).when(refreshTokenRepository).save(refreshToken);

        //TEST + RESULT
        Assertions.assertThrows(RuntimeException.class, () -> refreshTokenServiceImpl.addRefreshToken(refreshToken));

        verify(refreshTokenRepository, times(1)).save(refreshToken);
    }

    @Test
    void shouldGetRefreshToken_whenAllOk() {
        //PARAMETERS
        String refreshToken = "refreshToken";

        //MOCK
        doReturn(Optional.of(refreshToken)).when(refreshTokenRepository).findByRefreshToken(refreshToken);

        //TEST
        Optional<RefreshToken> result = refreshTokenServiceImpl.getRefreshToken(refreshToken);

        //RESULT
        Assertions.assertNotNull(result);
        verify(refreshTokenRepository, times(1)).findByRefreshToken(refreshToken);
    }

    @Test
    void shouldReturnEmptyOptional_whenNoRefreshTokenFound(){
        //PARAMETERS
        String refreshToken = "refreshToken";

        //MOCK
        doReturn(Optional.empty()).when(refreshTokenRepository).findByRefreshToken(refreshToken);

        //TEST
        Optional<RefreshToken> result = refreshTokenServiceImpl.getRefreshToken(refreshToken);

        //RESULTS
        Assertions.assertNotNull(result);
        verify(refreshTokenRepository, times(1)).findByRefreshToken(refreshToken);
    }

    @Test
    void shouldThrowException_whenFindRefreshTokenFails() {
        //PARAMETERS
        String refreshToken = "refreshToken";

        //MOCK
        doThrow(RuntimeException.class).when(refreshTokenRepository).findByRefreshToken(refreshToken);

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () -> refreshTokenServiceImpl.getRefreshToken(refreshToken));

        verify(refreshTokenRepository, times(1)).findByRefreshToken(refreshToken);
    }

    @Test
    void shouldInvalidateRefreshToken_whenAllOk() {
        //PARAMETERS
        String refreshToken = "refreshToken";

        //MOCK
        doNothing().when(refreshTokenRepository).invalidateRefreshToken(refreshToken);

        //TEST
        refreshTokenServiceImpl.invalidateRefreshToken(refreshToken);

        //RESULTS
        verify(refreshTokenRepository, times(1)).invalidateRefreshToken(refreshToken);
    }

    @Test
    void shouldThrowException_whenInvalidateRefreshTokenFails() {
        //PARAMETERS
        String refreshToken = "refreshToken";

        //MOCK
        doThrow(RuntimeException.class).when(refreshTokenRepository).invalidateRefreshToken(refreshToken);


        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () -> refreshTokenServiceImpl.invalidateRefreshToken(refreshToken));

        verify(refreshTokenRepository, times(1)).invalidateRefreshToken(refreshToken);
    }
}
