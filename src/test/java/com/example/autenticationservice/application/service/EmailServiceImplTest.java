package com.example.autenticationservice.application.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {

    @InjectMocks
    private EmailServiceImpl emailServiceImpl;

    @Mock
    private JavaMailSender javaMailSender;

    private String mailSenderAddress = "test@test.it";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailServiceImpl, "mailSenderAddress", mailSenderAddress);
    }

    @Test
    public void shouldSendEmail_whenAllOk(){
        //PARAMETERS
        String to = "toTest";
        String subject = "subjectTest";
        String body = "bodyTest";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        simpleMailMessage.setFrom(mailSenderAddress);

        //MOCK
        doNothing().when(javaMailSender).send(simpleMailMessage);

        //TEST
        emailServiceImpl.sendEmail(to,subject,body);

        //RESULTS
        verify(javaMailSender,times(1)).send(simpleMailMessage);

    }

    @Test
    public void shouldThrowException_whensSendEmailFail(){
        //PARAMETERS
        String to = "toTest";
        String subject = "subjectTest";
        String body = "bodyTest";

        //MOCK
        doThrow(RuntimeException.class).when(javaMailSender).send(any(SimpleMailMessage.class));

        //TEST + RESULTS
        Assertions.assertThrows(RuntimeException.class, () -> emailServiceImpl.sendEmail(to, subject, body));
    }
}
