package dev.coffeebeanteam.spotifyshare.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMail() {
        String testTo = "test@example.com";
        String testSubject = "Test Subject";
        String testBody = "Test Body";

        emailService.sendMail(testTo, testSubject, testBody);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
