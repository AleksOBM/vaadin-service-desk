package com.example.vsd.manager.model;

import lombok.Builder;

@Builder
public record MailConfigUpdateData(
		String email,
		String password,
		String notificationRecipient,
		boolean resetPassword,
		String smtpHost,
		Integer smtpPort,
		Boolean smtpAuth,
		Boolean smtpSsl
) {
}
