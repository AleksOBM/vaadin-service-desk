package com.example.application.backend.util.exception;

public class ConditionsNotMetException extends RuntimeException {
	public ConditionsNotMetException(String message) {
		super(message);
	}
}
