package com.example.vsd.manager.service.mail;

import com.example.vsd.manager.model.MailCredentials;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class MailSenderServiceTest {

	@Test
	void configuresSslWhenSslIsEnabled() {
		JavaMailSenderImpl sender = createSender(true);

		assertEquals("true", sender.getJavaMailProperties().getProperty("mail.smtp.ssl.enable"));
		assertEquals("false", sender.getJavaMailProperties().getProperty("mail.smtp.starttls.enable"));
	}

	@Test
	void configuresStartTlsWhenSslIsDisabled() {
		JavaMailSenderImpl sender = createSender(false);

		assertEquals("false", sender.getJavaMailProperties().getProperty("mail.smtp.ssl.enable"));
		assertEquals("true", sender.getJavaMailProperties().getProperty("mail.smtp.starttls.enable"));
	}

	private JavaMailSenderImpl createSender(boolean ssl) {
		MailSenderService service = new MailSenderService(mock(MailConfigService.class));
		return service.createSender(new MailCredentials("sender@example.com", "password",
				"recipient@example.com", "localhost", 1025, false, ssl));
	}
}
