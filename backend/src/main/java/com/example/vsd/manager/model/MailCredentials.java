package com.example.vsd.manager.model;

public record MailCredentials(
		String email,
		String password,
		String notificationRecipient,
		String smtpHost,
		int smtpPort,
		boolean smtpAuth,
		boolean smtpSsl
) {
}
