package com.example.vsd.manager.service.agent;

import com.example.vsd.manager.enity.Agent;
import com.example.vsd.manager.repository.AgentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgentServiceImplTest {

	private final AgentRepository agentRepository = mock(AgentRepository.class);
	private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
	private final UserDetailsService detailsService = mock(UserDetailsService.class);
	private final JwtEncoder jwtEncoder = mock(JwtEncoder.class);
	private final AgentServiceImpl service = new AgentServiceImpl(
			agentRepository, passwordEncoder, detailsService, jwtEncoder);

	@BeforeEach
	void setUp() {
		when(detailsService.loadUserByUsername("agent"))
				.thenReturn(User.withUsername("agent")
						.password("old-hash")
						.authorities("ROLE_AGENT")
						.build());
	}

	@Test
	void changesPasswordAfterVerifyingCurrentPassword() {
		var agent = Agent.builder().name("agent").passwordHash("old-hash").deleted(false).build();
		when(passwordEncoder.matches("old-password", "old-hash"))
				.thenReturn(true);
		when(passwordEncoder.encode("new-password")).thenReturn("new-hash");
		when(agentRepository.findByNameAndDeletedFalse("agent")).thenReturn(Optional.of(agent));

		service.changePassword("agent", "old-password", "new-password");

		assertEquals("new-hash", agent.getPasswordHash());
		verify(agentRepository).save(agent);
	}

	@Test
	void rejectsAnIncorrectCurrentPassword() {
		when(passwordEncoder.matches("wrong-password", "old-hash"))
				.thenReturn(false);

		assertThrows(BadCredentialsException.class,
				() -> service.changePassword(
						"agent", "wrong-password", "new-password"));

		verify(agentRepository, never()).save(any());
	}

	@Test
	void rejectsAdminPasswordChanges() {
		assertThrows(IllegalArgumentException.class,
				() -> service.changePassword(
						"admin", "current-password", "new-password"));

		verifyNoInteractions(detailsService, passwordEncoder, agentRepository);
	}
}
