package com.example.vsd.manager.service.mail;

import com.example.vsd.manager.enity.MailConfig;
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
		MailCredentials credentials = mailConfigRepository.findFirstByOrderByIdAsc()
				.map(this::toCredentials)
				.orElse(null);
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
		return credentials == null ? "" : credentials.notificationRecipient();
	}

	public void update(String newEmail, String newPassword, String newNotificationRecipient) {
		MailConfig config = mailConfigRepository.findFirstByOrderByIdAsc()
				.orElseGet(MailConfig::new);
		config.setEmailEncrypted(newEmail);
		config.setPasswordEncrypted(newPassword);
		config.setNotificationRecipient(newNotificationRecipient);
		mailConfigRepository.save(config);
		load();
	}

	private MailCredentials toCredentials(MailConfig config) {
		return new MailCredentials(config.getEmailEncrypted(), config.getPasswordEncrypted(),
				config.getNotificationRecipient());
	}
}
