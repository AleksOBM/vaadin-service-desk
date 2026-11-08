package com.example.vsd.manager.converter;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class EncryptedStringConverterTest {

	private final EncryptedStringConverter converter = new EncryptedStringConverter(
			Base64.getEncoder().encodeToString(
					"0123456789abcdef0123456789abcdef".getBytes(StandardCharsets.UTF_8)));

	@Test
	void roundTripAndDifferentCiphertexts() {
		String plainText = "secret-app-password";

		String firstEncrypted = converter.convertToDatabaseColumn(plainText);
		String secondEncrypted = converter.convertToDatabaseColumn(plainText);

		assertEquals(plainText, converter.convertToEntityAttribute(firstEncrypted));
		assertNotEquals(firstEncrypted, secondEncrypted);
		assertTrue(firstEncrypted.contains(":"));
	}

	@Test
	void rejectsShortKey() {
		String shortKey = Base64.getEncoder().encodeToString(new byte[16]);

		assertThrows(IllegalStateException.class, () -> new EncryptedStringConverter(shortKey));
	}
}
