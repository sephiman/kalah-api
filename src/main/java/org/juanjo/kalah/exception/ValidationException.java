package org.juanjo.kalah.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ValidationException that will return 400 BAD REQUEST
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ValidationException extends Exception {
	public ValidationException(String message) {
		super(message);
	}
}
