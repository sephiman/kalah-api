package org.juanjo.kalah.service;

import org.juanjo.kalah.dto.GameDTO;
import org.juanjo.kalah.exception.NotFoundException;
import org.juanjo.kalah.exception.ValidationException;

/**
 * Service that manages the games
 */
public interface GameService {

	/**
	 * Creates a new board for a kalah game
	 *
	 * @return created kalah game
	 */
	GameDTO createKalahGame();

	/**
	 * Retrieves the kalah game that is not finished
	 *
	 * @param gameId to search
	 * @return kalah game and url
	 * @throws NotFoundException
	 */
	GameDTO getNonFinishedById(long gameId) throws ValidationException;

	/**
	 * Updates status to finished
	 *
	 * @param gameId            to update
	 * @param firstPlayerScore  number of stones in first player house
	 * @param secondPlayerScore number of stones in second player house
	 */
	void finish(long gameId, int firstPlayerScore, int secondPlayerScore) throws NotFoundException;
}
