package br.com.tradeflow.exception;

import org.springframework.http.HttpStatus;

public class MessageKeyException extends RuntimeException {

	private String key;
	private Object[] args;
	private HttpStatus status = HttpStatus.BAD_REQUEST;

	public MessageKeyException(String message) {
		this(message, (Object[])null);
	}

	public MessageKeyException(String key, Object... args) {
		super(key);
		this.key = key;
		this.args = args;
	}

	public MessageKeyException(String key, HttpStatus status) {
		super(key);
		this.status = status;
	}

	public String getKey() {
		return key;
	}

	public Object[] getArgs() {
		return args;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
