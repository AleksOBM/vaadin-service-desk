package com.example.vsd.manager.service.mail;

import com.example.vsd.manager.converter.EncryptedStringConverter;
import com.example.vsd.manager.enity.MailConfig;
import com.example.vsd.manager.model.MailCredentials;
import com.example.vsd.manager.repository.MailConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class MailConfigService {

	private final MailConfigRepository mailConfigRepository;
	private final AtomicReference<MailCredentials> cache = new AtomicReference<>();

	public MailConfigService(MailConfigRepository mailConfigRepository) {
		this.mailConfigRepository = mailConfigRepository;
	}

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

	public void update(String newEmail, String newPassword, String newNotificationRecipient) {
		update(newEmail, newPassword, newNotificationRecipient, false);
	}

	public void update(String newEmail, String newPassword, String newNotificationRecipient,
	                   boolean resetPassword) {
		MailCredentials current = cache.get();
		String smtpHost = current == null ? "localhost" : current.smtpHost();
		int smtpPort = current == null ? 1025 : current.smtpPort();
		boolean smtpAuth = current != null && current.smtpAuth();
		boolean smtpSsl = current != null && current.smtpSsl();
		update(newEmail, newPassword, newNotificationRecipient, resetPassword,
				smtpHost, smtpPort, smtpAuth, smtpSsl);
	}

	public void update(String newEmail, String newPassword, String newNotificationRecipient,
	                   boolean resetPassword, String smtpHost, int smtpPort,
	                   boolean smtpAuth, boolean smtpSsl) {
		if (smtpHost == null || smtpHost.isBlank()) {
			throw new IllegalArgumentException("SMTP host is required");
		}
		if (smtpPort < 1 || smtpPort > 65535) {
			throw new IllegalArgumentException("SMTP port must be between 1 and 65535");
		}
		MailConfig config = mailConfigRepository.findFirstByOrderByIdAsc().orElseGet(MailConfig::new);
		boolean passwordProvided = newPassword != null && !newPassword.isBlank()
				&& !"********".equals(newPassword);
		if (resetPassword && !passwordProvided) {
			throw new IllegalArgumentException("New mail app password is required after changing sender email");
		}
		if (passwordProvided) {
			config.setPasswordEncrypted(newPassword);
		} else if (config.getPasswordEncrypted() == null || config.getPasswordEncrypted().isBlank()) {
			throw new IllegalArgumentException("Mail app password is required");
		}
		config.setEmailEncrypted(newEmail);
		config.setNotificationRecipient(newNotificationRecipient);
		config.setSmtpHost(smtpHost.trim());
		config.setSmtpPort(smtpPort);
		config.setSmtpAuth(smtpAuth);
		config.setSmtpSsl(smtpSsl);
		mailConfigRepository.save(config);
		load();
	}

	private MailCredentials toCredentials(MailConfig config) {
		return new MailCredentials(config.getEmailEncrypted(), config.getPasswordEncrypted(),
				config.getNotificationRecipient(),
				config.getSmtpHost() == null ? "localhost" : config.getSmtpHost(),
				config.getSmtpPort() == null ? 1025 : config.getSmtpPort(),
				Boolean.TRUE.equals(config.getSmtpAuth()),
				Boolean.TRUE.equals(config.getSmtpSsl()));
	}

	private boolean hasDemoSeed(MailConfig config) {
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

	private String removeDemoSeedPrefix(String value) {
		return value.substring(EncryptedStringConverter.DEMO_SEED_PREFIX.length());
	}
}
