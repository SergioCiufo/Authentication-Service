package com.example.autenticationservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AutenticationServiceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void start(){
        Assertions.assertDoesNotThrow(() -> AutenticationServiceApplication.main(new String[]{}));
    }

}
