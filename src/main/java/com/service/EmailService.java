package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationEmail(String toEmail, String name, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("aanchalagre19@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Account Registered Successfully");
        message.setText("Hi " + name + ",\n\nYour account has been registered successfully.\nYour password: " + password + "\n\nRegards,\nYour App Team");

        mailSender.send(message);
    }
}
