package com.example.vsd.manager.enity;

import com.example.vsd.manager.converter.EncryptedStringConverter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = "spring.test.database.replace=NONE")
@Import(EncryptedStringConverter.class)
class MailConfigPersistenceTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@Transactional
	void savesEncryptedValuesAndLoadsPlaintext() {
		MailConfig config = new MailConfig();
		config.setEmailEncrypted("user@example.com");
		config.setPasswordEncrypted("app-password-123");

		entityManager.persist(config);
		entityManager.flush();
		entityManager.clear();

		String rawEmail = (String) entityManager.createNativeQuery(
						"select email_encrypted from mail_config where id = :id")
				.setParameter("id", config.getId())
				.getSingleResult();

		assertNotEquals("user@example.com", rawEmail);
		assertTrue(rawEmail.contains(":"));

		MailConfig loaded = entityManager.find(MailConfig.class, config.getId());
		assertEquals("user@example.com", loaded.getEmailEncrypted());
		assertEquals("app-password-123", loaded.getPasswordEncrypted());
	}

}
