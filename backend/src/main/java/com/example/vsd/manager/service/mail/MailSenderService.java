package com.example.vsd.manager.service.mail;

import com.example.vsd.manager.model.MailCredentials;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailSenderService {

	private static final String SMTP_TIMEOUT_MILLIS = "15000";

	private final MailConfigService mailConfigService;

	public MailSenderService(MailConfigService mailConfigService) {
		this.mailConfigService = mailConfigService;
	}

	public void send(String to, String subject, String body) {
		MailCredentials credentials = mailConfigService.get();
		send(credentials, to, subject, body);
	}

	private void send(MailCredentials credentials, String to, String subject, String body) {
		JavaMailSenderImpl mailSender = createSender(credentials);

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(credentials.email());
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
	}

	JavaMailSenderImpl createSender(MailCredentials credentials) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(credentials.smtpHost());
		mailSender.setPort(credentials.smtpPort());
		mailSender.setUsername(credentials.email());
		mailSender.setPassword(credentials.password());

		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.auth", Boolean.toString(credentials.smtpAuth()));
		mailProperties.setProperty("mail.smtp.ssl.enable", Boolean.toString(credentials.smtpSsl()));
		mailProperties.setProperty("mail.smtp.starttls.enable",
				Boolean.toString(!credentials.smtpSsl()));
		mailProperties.setProperty("mail.smtp.connectiontimeout", SMTP_TIMEOUT_MILLIS);
		mailProperties.setProperty("mail.smtp.timeout", SMTP_TIMEOUT_MILLIS);
		mailProperties.setProperty("mail.smtp.writetimeout", SMTP_TIMEOUT_MILLIS);
		mailSender.setJavaMailProperties(mailProperties);
		return mailSender;
	}
}
