package com.example.vsd.frontend.grpc.client;

import com.example.vsd.frontend.model.LoginResult;
import com.example.vsd.grpc.messages.LoginRequest;
import com.example.vsd.grpc.services.free.FreeControllerGrpc.FreeControllerBlockingStub;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginService {

	FreeControllerBlockingStub freeStub;

	public LoginResult login(String username, String password) {
		var loginResponse = freeStub.login(
				LoginRequest.newBuilder().setUsername(username)
						.setPassword(password).build());
		return LoginResult.builder()
				.accessToken(loginResponse.getAccessToken())
				.expiresIn(loginResponse.getExpiresIn())
				.authorities(loginResponse.getAuthoritiesList())
				.build();
	}
}
