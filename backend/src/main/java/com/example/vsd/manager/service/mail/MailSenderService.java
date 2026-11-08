package com.example.vsd.manager.service.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailSenderService {

    private static final String SMTP_TIMEOUT_MILLIS = "5000";

    private final MailConfigService mailConfigService;
    private final String host;
    private final int port;
    private final boolean smtpAuth;
    private final boolean startTlsEnabled;

    public MailSenderService(
            MailConfigService mailConfigService,
            @Value("${spring.mail.host}") String host,
            @Value("${spring.mail.port}") int port,
            @Value("${spring.mail.properties.mail.smtp.auth:true}") boolean smtpAuth,
            @Value("${spring.mail.properties.mail.smtp.starttls.enable:true}") boolean startTlsEnabled) {
        this.mailConfigService = mailConfigService;
        this.host = host;
        this.port = port;
        this.smtpAuth = smtpAuth;
        this.startTlsEnabled = startTlsEnabled;
    }

    public void send(String to, String subject, String body) {
        MailCredentials credentials = mailConfigService.get();
        send(credentials, to, subject, body);
    }

    private void send(MailCredentials credentials, String to, String subject, String body) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(credentials.email());
        mailSender.setPassword(credentials.password());

        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.auth", Boolean.toString(smtpAuth));
        mailProperties.setProperty("mail.smtp.starttls.enable", Boolean.toString(startTlsEnabled));
        mailProperties.setProperty("mail.smtp.connectiontimeout", SMTP_TIMEOUT_MILLIS);
        mailProperties.setProperty("mail.smtp.timeout", SMTP_TIMEOUT_MILLIS);
        mailProperties.setProperty("mail.smtp.writetimeout", SMTP_TIMEOUT_MILLIS);
        mailSender.setJavaMailProperties(mailProperties);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(credentials.email());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
