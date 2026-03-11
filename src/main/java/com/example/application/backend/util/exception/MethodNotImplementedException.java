package com.example.application.backend.util.exception;

public class MethodNotImplementedException extends RuntimeException {
	public MethodNotImplementedException() {
		super("Эта функция еще не реализована.");
	}
}
