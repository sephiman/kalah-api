package org.juanjo.kalah.service;

import org.juanjo.kalah.dto.GameStatusDTO;
import org.juanjo.kalah.exception.ValidationException;

/**
 * Service that manages the pit moves
 */
public interface PitService {

	/**
	 * Performs the movement on the board following the Kalah rules
	 *
	 * @param gameId to play
	 * @param pitId  to move
	 * @return the status of the board after the movement
	 * @throws ValidationException in case the movement is not valid
	 */
	GameStatusDTO move(long gameId, int pitId) throws ValidationException;
}
