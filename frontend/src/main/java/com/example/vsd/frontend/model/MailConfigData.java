package com.example.vsd.frontend.model;

import lombok.Builder;

@Builder
public record MailConfigData(
		String email,
		String password,
		String notificationRecipient,
		boolean resetPassword,
		String smtpHost,
		int smtpPort,
		boolean smtpAuth,
		boolean smtpSsl
) {
}
