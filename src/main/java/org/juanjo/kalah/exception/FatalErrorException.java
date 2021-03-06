package org.juanjo.kalah.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * FatalErrorException that will return 500 Internal server error
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
@NoArgsConstructor
public class FatalErrorException extends RuntimeException {
	public FatalErrorException(Throwable cause) {
		super(cause);
	}
}
