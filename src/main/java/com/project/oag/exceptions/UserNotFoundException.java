package com.project.oag.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UserNotFoundException extends RuntimeException {
	private int status;

	public UserNotFoundException(HttpStatus status, String message) {
		super(message);
		this.status = status.value();
	}

	public UserNotFoundException(String message) {
		super(message);
		this.status = HttpStatus.UNPROCESSABLE_ENTITY.value();
	}
}

