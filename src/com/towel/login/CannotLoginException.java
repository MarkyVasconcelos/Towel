package com.towel.login;

public class CannotLoginException extends Exception {
	private String msg;

	public CannotLoginException(String msg) {
		this.msg = msg;
	}

	public String getMessage() {
		return msg;
	}
}
