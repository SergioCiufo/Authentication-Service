package com.example.autenticationservice.domain.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@ExtendWith(MockitoExtension.class)
public class HashUtilTest {
    @InjectMocks
    private HashUtil hashUtil;

    @Test
    public void shouldStringToSha1_whenAllOk(){
        //PARAMETERS
        String password = "pswTest";

        //TEST
        String results = hashUtil.stringToSha1(password);

        //RESULTS
        Assertions.assertNotNull(results);
        Assertions.assertFalse(results.isEmpty());
    }

    @Test
    public void shouldThrowException_whenStringToSha1Fails(){
        //MOCK
        try(MockedStatic<MessageDigest> md = Mockito.mockStatic(MessageDigest.class)) {
            md.when(() -> MessageDigest.getInstance("SHA-1"))
                    .thenThrow(new NoSuchAlgorithmException());

            Assertions.assertThrows(RuntimeException.class, () -> hashUtil.stringToSha1("pswTest"));
        }
    }
}
