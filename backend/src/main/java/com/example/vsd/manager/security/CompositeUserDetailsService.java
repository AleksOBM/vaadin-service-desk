package com.example.vsd.manager.security;

import com.example.vsd.manager.enity.Agent;
import com.example.vsd.manager.repository.AgentRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompositeUserDetailsService implements UserDetailsService {

	private final AgentRepository agentRepository;
	private final String adminPasswordHash;

	public CompositeUserDetailsService(AgentRepository repo,
	                                   PasswordEncoder encoder,
	                                   @Value("${vsd.admin.password}")
	                                   String adminPassword) {
		this.agentRepository = repo;
		this.adminPasswordHash = encoder.encode(adminPassword);
	}

	@Override
	@Transactional(readOnly = true)
	public @NonNull UserDetails loadUserByUsername(@NonNull String username) {
		if ("admin".equalsIgnoreCase(username)) {
			return User.withUsername("admin")
					.password(adminPasswordHash)
					.authorities("ROLE_ADMIN", "ROLE_AGENT")
					.build();
		}

		Agent agent = agentRepository
				.findByNameAndDeletedFalse(username)
				.orElseThrow(() -> new UsernameNotFoundException("Agent not found"));

		return User.withUsername(agent.getName())
				.password(agent.getPasswordHash())
				.authorities("ROLE_AGENT")
				.build();
	}
}