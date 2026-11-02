package com.example.vsd.frontend.security;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtClientInterceptor implements ClientInterceptor {

	@Override
	public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
			MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
		return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
			@Override
			public void start(Listener<RespT> responseListener, Metadata headers) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();

				String token = null;
				if (auth != null && auth.getDetails()
						instanceof GrpcAuthenticationProvider.AuthDetails details) {
					token = details.accessToken();
				}

				log.debug("JWT interceptor: method={}, auth={}, hasToken={}",
						method.getFullMethodName(),
						auth != null ? auth.getName() : "null",
						token != null && !token.isBlank());

				if (token != null && !token.isBlank()) {
					headers.put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER),
							"Bearer " + token);
				}
				super.start(responseListener, headers);
			}
		};
	}
}
