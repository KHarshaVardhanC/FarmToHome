package com.ftohbackend.servicetesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.ftohbackend.dto.MailBody;
import com.ftohbackend.service.EmailService;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMail_shouldSendEmail() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Message";
        MailBody mailBody = new MailBody(to, subject, text);

        // Act
        emailService.sendMail(mailBody);

        // Assert
        verify(javaMailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();
        
        assertEquals(to, capturedMessage.getTo()[0]);
        assertEquals(subject, capturedMessage.getSubject());
        assertEquals(text, capturedMessage.getText());
    }

    @Test
    void sendOtpEmail_shouldSendEmailWithOtp() {
        // Arrange
        String to = "test@example.com";
        String subject = "OTP Verification";
        String otp = "123456";

        // Act
        emailService.sendOtpEmail(to, subject, otp);

        // Assert
        verify(javaMailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();
        
        assertEquals(to, capturedMessage.getTo()[0]);
        assertEquals(subject, capturedMessage.getSubject());
        assertEquals("Your OTP is: " + otp, capturedMessage.getText());
    }
}