package org.juanjo.kalah.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * NoFoundException that will return 404 NOT FOUND
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends ValidationException {
	public NotFoundException(String message) {
		super(message);
	}
}
