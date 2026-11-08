package com.example.vsd.manager.service.mail;

import com.example.vsd.manager.model.MailCredentials;
import com.example.vsd.manager.model.MailMessage;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailSenderService {

	private final String smtpTimeoutMillis;
	private final MailConfigService mailConfigService;

	public MailSenderService(
			@Value("${vsd.smtp.timeout-millis:1500}") String smtpTimeoutMillis,
			MailConfigService mailConfigService) {
		this.smtpTimeoutMillis = smtpTimeoutMillis;
		this.mailConfigService = mailConfigService;
	}

	public void send(@NonNull MailMessage message) {
		var credentials = mailConfigService.get();
		JavaMailSenderImpl mailSender = createSender(credentials);
		message.from(credentials.email());
		mailSender.send(message.toSimpleMailMessage());
	}

	JavaMailSenderImpl createSender(@NonNull MailCredentials credentials) {
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
		mailProperties.setProperty("mail.smtp.connectiontimeout", smtpTimeoutMillis);
		mailProperties.setProperty("mail.smtp.timeout", smtpTimeoutMillis);
		mailProperties.setProperty("mail.smtp.writetimeout", smtpTimeoutMillis);
		mailSender.setJavaMailProperties(mailProperties);
		return mailSender;
	}
}
