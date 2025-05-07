package com.ftohbackend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ftohbackend.dto.MailBody;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(MailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        
//        message.setFrom("mahmadfarha9@gmail.com");
        message.setTo(mailBody.getTo());
        message.setSubject(mailBody.getSubject());
        message.setText(mailBody.getText());
        javaMailSender.send(message);
    }

    public void sendOtpEmail(String toEmail, String subject, String otp) {
        MailBody body = new MailBody(toEmail, subject, "Your OTP is: " + otp);
        sendMail(body);
    }
}

