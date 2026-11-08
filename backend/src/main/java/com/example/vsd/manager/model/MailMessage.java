package com.example.vsd.manager.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.mail.SimpleMailMessage;

@Data
@Builder
@Accessors(fluent = true)
public class MailMessage {

	private String from;
	private String to;
	private String subject;
	private String text;

	public SimpleMailMessage toSimpleMailMessage() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(this.from);
		message.setTo(this.to);
		message.setSubject(this.subject);
		message.setText(this.text);
		return message;
	}
}
