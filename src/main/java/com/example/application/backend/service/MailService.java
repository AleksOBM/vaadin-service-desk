package com.example.application.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

//    private final JavaMailSender mailSender;

    public void sendMassage(String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String THIS_EMAIL = "myMail@gmail.com";
        mailMessage.setFrom(THIS_EMAIL);
        String TARGET_EMAIL = "myMail@yandex.ru";
        mailMessage.setTo(TARGET_EMAIL);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
//        mailSender.send(mailMessage);
    }
}

/*
todo: Добавить настройки в application.yaml

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
 */