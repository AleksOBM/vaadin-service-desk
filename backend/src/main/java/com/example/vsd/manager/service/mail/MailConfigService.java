package com.example.vsd.manager.service.mail;

import com.example.vsd.manager.converter.EncryptedStringConverter;
import com.example.vsd.manager.enity.MailConfig;
import com.example.vsd.manager.model.MailConfigUpdateData;
import com.example.vsd.manager.model.MailCredentials;
import com.example.vsd.manager.repository.MailConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailConfigService {

	MailConfigRepository mailConfigRepository;
	AtomicReference<MailCredentials> cache = new AtomicReference<>();

	@PostConstruct
	public void load() {
		MailConfig config = mailConfigRepository.findFirstByOrderByIdAsc().orElse(null);
		if (config != null && hasDemoSeed(config)) {
			config.setEmailEncrypted(removeDemoSeedPrefix(config.getEmailEncrypted()));
			config.setPasswordEncrypted(removeDemoSeedPrefix(config.getPasswordEncrypted()));
			config.setNotificationRecipient(removeDemoSeedPrefix(config.getNotificationRecipient()));
			config = mailConfigRepository.save(config);
		}
		MailCredentials credentials = config == null ? null : toCredentials(config);
		cache.set(credentials);
		log.info("Mail configuration loaded: {}", credentials != null);
	}

	public MailCredentials get() {
		MailCredentials credentials = cache.get();
		if (credentials == null) {
			throw new IllegalStateException("Mail config not initialized");
		}
		return credentials;
	}

	public String getNotificationRecipient() {
		MailCredentials credentials = cache.get();
		return credentials == null || credentials.notificationRecipient() == null
				? "" : credentials.notificationRecipient();
	}

	public String getEmail() {
		MailCredentials credentials = cache.get();
		return credentials == null || credentials.email() == null ? "" : credentials.email();
	}

	public boolean isPasswordConfigured() {
		MailCredentials credentials = cache.get();
		return credentials != null && credentials.password() != null
				&& !credentials.password().isBlank();
	}

	public String getSmtpHost() {
		MailCredentials credentials = cache.get();
		return credentials == null ? "localhost" : credentials.smtpHost();
	}

	public int getSmtpPort() {
		MailCredentials credentials = cache.get();
		return credentials == null ? 1025 : credentials.smtpPort();
	}

	public boolean isSmtpAuth() {
		MailCredentials credentials = cache.get();
		return credentials != null && credentials.smtpAuth();
	}

	public boolean isSmtpSsl() {
		MailCredentials credentials = cache.get();
		return credentials != null && credentials.smtpSsl();
	}

	public void update(@NonNull MailConfigUpdateData updateData) {
		MailCredentials current = cache.get();
		String currentSmtpHost = current == null ? "localhost" : current.smtpHost();
		int currentSmtpPort = current == null ? 1025 : current.smtpPort();
		boolean currentSmtpAuth = current != null && current.smtpAuth();
		boolean currentSmtpSsl = current != null && current.smtpSsl();
		String smtpHost = updateData.smtpHost() == null
				? currentSmtpHost
				: updateData.smtpHost();
		int smtpPort = updateData.smtpPort() == null
				? currentSmtpPort
				: updateData.smtpPort();
		boolean smtpAuth = updateData.smtpAuth() == null
				? currentSmtpAuth
				: updateData.smtpAuth();
		boolean smtpSsl = updateData.smtpSsl() == null
				? currentSmtpSsl
				: updateData.smtpSsl();
		if (smtpHost == null || smtpHost.isBlank()) {
			throw new IllegalArgumentException("SMTP host is required");
		}
		if (smtpPort < 1 || smtpPort > 65535) {
			throw new IllegalArgumentException("SMTP port must be between 1 and 65535");
		}
		MailConfig config = mailConfigRepository.findFirstByOrderByIdAsc().orElseGet(MailConfig::new);
		boolean passwordProvided = updateData.password() != null && !updateData.password().isBlank()
				&& !"********".equals(updateData.password());
		if (updateData.resetPassword() && !passwordProvided) {
			throw new IllegalArgumentException("New mail app password is required after changing sender email");
		}
		if (passwordProvided) {
			config.setPasswordEncrypted(updateData.password());
		} else if (config.getPasswordEncrypted() == null || config.getPasswordEncrypted().isBlank()) {
			throw new IllegalArgumentException("Mail app password is required");
		}
		config.setEmailEncrypted(updateData.email());
		config.setNotificationRecipient(updateData.notificationRecipient());
		config.setSmtpHost(smtpHost.trim());
		config.setSmtpPort(smtpPort);
		config.setSmtpAuth(smtpAuth);
		config.setSmtpSsl(smtpSsl);
		mailConfigRepository.save(config);
		load();
	}

	@NonNull
	private MailCredentials toCredentials(@NonNull MailConfig config) {
		return MailCredentials.builder()
				.email(config.getEmailEncrypted())
				.password(config.getPasswordEncrypted())
				.notificationRecipient(config.getNotificationRecipient())
				.smtpHost(config.getSmtpHost() == null ? "localhost" : config.getSmtpHost())
				.smtpPort(config.getSmtpPort() == null ? 1025 : config.getSmtpPort())
				.smtpAuth(Boolean.TRUE.equals(config.getSmtpAuth()))
				.smtpSsl(Boolean.TRUE.equals(config.getSmtpSsl()))
				.build();
	}

	private boolean hasDemoSeed(@NonNull MailConfig config) {
		boolean emailSeed = isDemoSeed(config.getEmailEncrypted());
		boolean passwordSeed = isDemoSeed(config.getPasswordEncrypted());
		boolean recipientSeed = isDemoSeed(config.getNotificationRecipient());
		if (emailSeed || passwordSeed || recipientSeed) {
			if (!(emailSeed && passwordSeed && recipientSeed)) {
				throw new IllegalStateException("Mail configuration contains an incomplete demo seed");
			}
			return true;
		}
		return false;
	}

	private boolean isDemoSeed(String value) {
		return value != null && value.startsWith(EncryptedStringConverter.DEMO_SEED_PREFIX);
	}

	@NonNull
	private String removeDemoSeedPrefix(@NonNull String value) {
		return value.substring(EncryptedStringConverter.DEMO_SEED_PREFIX.length());
	}
}
