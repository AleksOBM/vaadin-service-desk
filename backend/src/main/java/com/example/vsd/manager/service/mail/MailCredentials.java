package com.example.vsd.manager.service.mail;

public record MailCredentials(
		String email,
		String password,
		String notificationRecipient
) {
}
