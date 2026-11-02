package com.example.vsd.manager.service.agent;

import com.example.vsd.grpc.messages.AgentProto;
import com.example.vsd.grpc.messages.LoginResponse;
import com.example.vsd.manager.enity.Agent;
import com.example.vsd.manager.exception.NotFoundException;
import com.example.vsd.manager.exception.ValidationException;
import com.example.vsd.manager.mapper.AgentMapper;
import com.example.vsd.manager.repository.AgentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AgentServiceImpl implements AgentService {

	AgentRepository agentRepository;
	PasswordEncoder passwordEncoder;
	UserDetailsService detailsService;
	JwtEncoder jwtEncoder;

	@Override
	public void saveAgent(@NonNull AgentProto proto) {

		// если имя не задано
		if (!proto.hasName()) {
			throw new ValidationException("Agent name is required");
		}

		// если имя уже есть
		if (agentRepository.existsByName(proto.getName())) {
			throw new IllegalArgumentException("Agent already exists");
		}

		// если имя admin
		if (proto.getName().equalsIgnoreCase("admin")) {
			throw new IllegalArgumentException("Agent name must not be admin");
		}

		// если это новая запись
		if (!proto.hasId()) {
			var agent = AgentMapper.toEntity(proto);
			// TODO: 01.11.2026 придумывать пароль при создании
			agent.setPasswordHash(passwordEncoder.encode(proto.getName()));
			agentRepository.save(agent);
			return;
		}

		// обновление
		var agent = getAgentById(proto.getId());
		agent.setName(proto.getName());
		if (proto.hasPassword()) {
			agent.setPasswordHash(passwordEncoder.encode(proto.getPassword()));
		}
		agentRepository.save(agent);
	}

	@Override
	public List<AgentProto> findAgents(String text) {
		return agentRepository.findAgents(text).stream()
				.map(AgentMapper::toProto)
				.toList();
	}

	@Override
	public List<AgentProto> getAllAgents() {
		return agentRepository.findAllByDeletedFalse().stream()
				.sorted(Comparator.comparingLong(Agent::getId))
				.map(AgentMapper::toProto)
				.toList();
	}

	@Override
	public void setAgentDeleted(long agentId) {
		var agent = getAgentById(agentId);
		if (agent.isDeleted()) {
			throw new IllegalStateException(
					"Сотрудник с id=%s удален. Проверьте корзину."
							.formatted(agentId));
		}
		agent.setDeleted(true);
		agentRepository.save(agent);
	}

	@Override
	public LoginResponse loginAgent(@NonNull String username, String password) {
		if (username.isBlank()) {
			throw new ValidationException("Username is required");
		}
		if (password.isBlank()) {
			throw new ValidationException("Password is required");
		}

		UserDetails user;
		try {
			user = detailsService.loadUserByUsername(username);
		} catch (UsernameNotFoundException e) {
			throw new BadCredentialsException("Wrong username or password");
		}

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException("Wrong password");
		}

		List<String> roles = user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.toList();

		return createLoginResponse(user.getUsername(), roles);
	}

	private @NonNull LoginResponse createLoginResponse(String agentName, @NonNull List<String> roles) {
		Instant now = Instant.now();
		long ttlSeconds = 3600;

		var claims = JwtClaimsSet.builder()
				.issuer("vaadin-service-desk")
				.issuedAt(now)
				.expiresAt(now.plusSeconds(ttlSeconds))
				.subject(agentName)
				.claim("authorities", roles.stream().toList())
				.build();

		var header = JwsHeader.with(MacAlgorithm.HS256).build();
		String accessToken = jwtEncoder.encode(
				JwtEncoderParameters.from(header, claims)
		).getTokenValue();

		return LoginResponse.newBuilder()
				.setAccessToken(accessToken)
				.setExpiresIn(ttlSeconds)
				.addAllAuthorities(roles)
				.build();
	}

	@Override
	public void restoreAgent(Long agentId) {
		var agent = getAgentById(agentId);
		agent.setDeleted(false);
		agentRepository.save(agent);
	}

	@Override
	public void deleteAgent(Long agentId) {
		agentRepository.deleteById(agentId);
	}

	@NonNull
	private Agent getAgentById(long agentId) {
		return agentRepository.findById(agentId)
				.orElseThrow(() -> new NotFoundException(
						"Сотрудник с id=%s не найден".formatted(agentId)));
	}
}
