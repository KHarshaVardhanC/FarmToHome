package com.ftohbackend.servicetesting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ftohbackend.model.Mails;
import com.ftohbackend.repository.MailRepository;
import com.ftohbackend.service.MailServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MailServiceTest {

    @Mock
    private MailRepository mailRepository;

    @InjectMocks
    private MailServiceImpl mailService;

    private Mails mail;
    private String validEmailId;

    @BeforeEach
    public void setUp() {
        validEmailId = "test@example.com";
        mail = new Mails();
        mail.setMailId(validEmailId);
        // Initialize other fields if needed
    }

    @AfterEach
    public void tearDown() {
        mail = null;
    }

    @Test
    @DisplayName("JUnit test for isMailExists operation when mail exists")
    public void givenEmailId_whenIsMailExists_thenReturnTrue() {
        // given - precondition or setup
        given(mailRepository.existsById(validEmailId)).willReturn(true);
        
        // when - action or the behavior
        Boolean exists = mailService.isMailExists(validEmailId);
        
        // then - verify the output
        assertThat(exists).isTrue();
        verify(mailRepository, times(1)).existsById(validEmailId);
    }

    @Test
    @DisplayName("JUnit test for isMailExists operation when mail does not exist")
    public void givenEmailId_whenIsMailExists_thenReturnFalse() {
        // given - precondition or setup
        given(mailRepository.existsById(validEmailId)).willReturn(false);
        
        // when - action or the behavior
        Boolean exists = mailService.isMailExists(validEmailId);
        
        // then - verify the output
        assertThat(exists).isFalse();
        verify(mailRepository, times(1)).existsById(validEmailId);
    }

    @Test
    @DisplayName("JUnit test for isMailExists operation with null email")
    public void givenNullEmailId_whenIsMailExists_thenHandleAppropriately() {
        // given - precondition or setup
        String nullEmail = null;
        given(mailRepository.existsById(nullEmail)).willReturn(false);
        
        // when - action or the behavior
        Boolean exists = mailService.isMailExists(nullEmail);
        
        // then - verify the output
        assertThat(exists).isFalse();
        verify(mailRepository, times(1)).existsById(nullEmail);
    }

    @Test
    @DisplayName("JUnit test for addMail operation")
    public void givenMailsObject_whenAddMail_thenRepositorySaveMethodCalled() {
        // given - precondition or setup
        given(mailRepository.save(mail)).willReturn(mail);
        
        // when - action or the behavior
        mailService.addMail(mail);
        
        // then - verify the output
        verify(mailRepository, times(1)).save(mail);
    }

    @Test
    @DisplayName("JUnit test for addMail operation with null mail")
    public void givenNullMailsObject_whenAddMail_thenHandleAppropriately() {
        // when - action or the behavior & then - verify the output
        assertThrows(NullPointerException.class, () -> {
            mailService.addMail(null);
        });
        
        verify(mailRepository, never()).save(any(Mails.class));
    }
}
