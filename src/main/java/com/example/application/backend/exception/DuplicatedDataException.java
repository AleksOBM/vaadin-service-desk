package com.example.application.backend.exception;

public class DuplicatedDataException extends RuntimeException {
	public DuplicatedDataException(String message) {
		super(message);
	}
}
