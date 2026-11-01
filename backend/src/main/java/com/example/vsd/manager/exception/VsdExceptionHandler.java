package com.example.vsd.manager.exception;

import io.grpc.Status;
import org.springframework.grpc.server.advice.GrpcAdvice;
import org.springframework.grpc.server.advice.GrpcExceptionHandler;
import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("unused")
@GrpcAdvice
public class VsdExceptionHandler {

	@GrpcExceptionHandler({
			IllegalArgumentException.class,
			IllegalStateException.class,
			ValidationException.class
	})
	public Status handleBadRequest(Exception ex) {
		return Status.INVALID_ARGUMENT.withDescription(ex.getMessage());
	}

	@GrpcExceptionHandler(NotFoundException.class)
	public Status handleNotFound(NotFoundException ex) {
		return Status.NOT_FOUND.withDescription(ex.getMessage());
	}

	@GrpcExceptionHandler(AuthenticationException.class)
	public Status handleAuthentication(AuthenticationException ex) {
		return Status.UNAUTHENTICATED.withDescription(ex.getMessage());
	}

}
