package org.juanjo.kalah.service;

import org.juanjo.kalah.dao.Board;

/**
 * Service that manages the boards
 */
public interface BoardService {

	/**
	 * Generates the default board for a game
	 *
	 * @param gameId to associate
	 */
	void initializeBoard(long gameId);


	/**
	 * Returns the latest board given the game id
	 *
	 * @param gameId to filter
	 * @return board
	 */
	Board getLatestBoard(long gameId);

	/**
	 * Adds a new board to the given game
	 *
	 * @param gameId                to associated with
	 * @param lastMovedPit          from last move
	 * @param stonesBeforeMovingPit before moving pit
	 * @param pits                  new board
	 * @return created board
	 */
	Board addBoardToGame(long gameId, int lastMovedPit, int stonesBeforeMovingPit, int[] pits);
}
