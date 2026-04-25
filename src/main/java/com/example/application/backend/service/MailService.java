package com.example.application.backend.service;

import com.example.application.backend.model.MailEntity;
import com.example.application.backend.repository.MailRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailService {

    MailRepository mailRepository;

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

    public Optional<MailEntity> getMailEntity() {
        return mailRepository.findById(1L);
    }

    public void saveMailEntity(MailEntity mailEntity) {
        mailRepository.save(mailEntity);
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