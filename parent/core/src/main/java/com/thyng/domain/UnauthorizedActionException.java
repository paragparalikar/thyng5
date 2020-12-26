package com.thyng.domain;

public class UnauthorizedActionException extends RuntimeException {
	private static final long serialVersionUID = -5248603839969102945L;

	public UnauthorizedActionException() {
	}

	public UnauthorizedActionException(String message) {
		super(message);
	}

	public UnauthorizedActionException(Throwable cause) {
		super(cause);
	}

	public UnauthorizedActionException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnauthorizedActionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
