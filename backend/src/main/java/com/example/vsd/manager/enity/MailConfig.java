package com.example.vsd.manager.enity;

import com.example.vsd.manager.converter.EncryptedStringConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "mail_config")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MailConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Convert(converter = EncryptedStringConverter.class)
	String emailEncrypted;

	@Convert(converter = EncryptedStringConverter.class)
	String passwordEncrypted;

	@Convert(converter = EncryptedStringConverter.class)
	String notificationRecipient;

	String smtpHost;

	Integer smtpPort;

	Boolean smtpAuth;

	Boolean smtpSsl;

	Instant updatedAt;
}
