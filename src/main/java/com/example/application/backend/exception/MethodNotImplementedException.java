package com.example.application.backend.exception;

public class MethodNotImplementedException extends RuntimeException {
	public MethodNotImplementedException() {
		super("Эта функция еще не реализована.");
	}
}
