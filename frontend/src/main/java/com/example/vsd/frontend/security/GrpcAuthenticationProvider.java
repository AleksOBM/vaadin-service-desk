package com.example.vsd.frontend.security;

import com.example.vsd.frontend.grpc.client.LoginService;
import com.example.vsd.frontend.model.LoginResult;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class GrpcAuthenticationProvider implements AuthenticationProvider {

	private final LoginService loginService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = Objects.requireNonNull(authentication.getCredentials()).toString();

		LoginResult result;
		try {
			result = loginService.login(username, password);
		} catch (StatusRuntimeException e) {
			log.warn("gRPC login failed: status={}, description={}",
					e.getStatus().getCode(), e.getStatus().getDescription());
			throw new BadCredentialsException("Внешний сервис отклонил вход", e);
		} catch (Exception e) {
			throw new AuthenticationServiceException("Ошибка вызова сервиса аутентификации", e);
		}

		return getToken(result, username);
	}

	private static @NonNull UsernamePasswordAuthenticationToken getToken(LoginResult result,
	                                                                     String username) {

		if (result.accessToken() == null || result.accessToken().isBlank()) {
			throw new BadCredentialsException("Внешний сервис не вернул access_token");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String role : result.authorities()) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		if (authorities.isEmpty()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}

		UserDetails principal = User.withUsername(username)
				.password("")
				.authorities(authorities)
				.build();

		// credentials = null, чтобы Spring Security не пытался их стирать/сериализовать
		var token = new UsernamePasswordAuthenticationToken(principal, null, authorities);
		token.setDetails(new AuthDetails(result.accessToken(), result.expiresIn()));
		return token;
	}

	@Override
	public boolean supports(@NonNull Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	public record AuthDetails(String accessToken, long expiresInSeconds) {
	}

}
