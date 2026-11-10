package com.example.vsd.manager.enity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

class LastModifiedByListenerTest {

	private final LastModifiedByListener listener = new LastModifiedByListener();

	@AfterEach
	void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void recordsAuthenticatedUsername() {
		SecurityContextHolder.getContext().setAuthentication(
				UsernamePasswordAuthenticationToken.authenticated(
						"agent-name",
						null, java.util.List.of()
				));
		var entity = new Agent();

		listener.setLastModifiedBy(entity);

		assertEquals("agent-name", entity.getLastModifiedBy());
	}

	@Test
	void rejectsChangesWithoutAuthenticatedUser() {
		assertThrows(IllegalStateException.class, () -> listener.setLastModifiedBy(new Agent()));
	}
}
