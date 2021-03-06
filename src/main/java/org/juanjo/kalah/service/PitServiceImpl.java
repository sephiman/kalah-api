package org.juanjo.kalah.service;

import lombok.extern.slf4j.Slf4j;
import org.juanjo.kalah.dao.Board;
import org.juanjo.kalah.dto.GameDTO;
import org.juanjo.kalah.dto.GameStatusDTO;
import org.juanjo.kalah.exception.NotFoundException;
import org.juanjo.kalah.exception.ValidationException;
import org.juanjo.kalah.utils.GameMapper;
import org.juanjo.kalah.utils.KalahConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Slf4j
public class PitServiceImpl implements PitService {
	private final GameService gameService;
	private final BoardService boardService;

	@Autowired
	public PitServiceImpl(GameService gameService, BoardService boardService) {
		this.gameService = gameService;
		this.boardService = boardService;
	}

	@Override
	@Transactional
	public GameStatusDTO move(long gameId, int pitId) throws ValidationException {
		int actualPitMove = pitId - 1;
		log.info("Trying move {} for game {}", actualPitMove, gameId);
		GameDTO game = gameService.getNonFinishedById(gameId);
		Board currentBoard = boardService.getLatestBoard(gameId);

		validatePitMove(currentBoard, actualPitMove);

		int[] pits = getNewPits(gameId, currentBoard.getPits(), actualPitMove);

		return GameMapper.getGameStatus(gameId, game.getUrl(), pits);
	}

	/**
	 * The movement is valid if:
	 * - Selected pit is among the valid movement pits
	 * - The pit is not empty
	 * - It is the turn of the pit's player
	 *
	 * @param currentBoard
	 * @param actualPitMove
	 * @throws ValidationException in case the movement is not valid
	 */
	private void validatePitMove(Board currentBoard, int actualPitMove) throws ValidationException {
		validatePitRange(actualPitMove);

		if (isFirstMove(currentBoard)) {
			return;
		}

		validatePitEmpty(currentBoard.getPits(), actualPitMove);

		validatePitBelongsToPlayerTurn(actualPitMove, currentBoard.getPitLastMove(), currentBoard.getStonesLastMove());
	}

	/**
	 * Validates whether the pit movement belongs to a player's pit.
	 *
	 * @param actualPitMove
	 * @throws ValidationException
	 */
	private void validatePitRange(int actualPitMove) throws ValidationException {
		if (!KalahConstants.VALID_PIT_MOVEMENTS.contains(actualPitMove)) {
			log.info("Pit not allowed");
			throw new ValidationException("Pit cannot be moved");
		}
	}

	/**
	 * Validates that the pit to move is not empty
	 *
	 * @param pits
	 * @param actualPitMove
	 * @throws ValidationException
	 */
	private void validatePitEmpty(int[] pits, int actualPitMove) throws ValidationException {
		if (pits[actualPitMove] == 0) {
			log.info("Pit is empty");
			throw new ValidationException("Pit is empty");
		}
	}

	/**
	 * Validates that the current pit movement belongs to the player that is the turn.
	 *
	 * @param actualPitMove
	 * @param pitLastMove
	 * @param stonesLastMove
	 * @throws ValidationException
	 */
	private void validatePitBelongsToPlayerTurn(int actualPitMove, Integer pitLastMove, Integer stonesLastMove) throws ValidationException {
		// Is the turn for the actual pit move if player 1 was and ended in his house, only player one is allowed to move
		boolean isFirstPlayerMovingNow = isFirstPlayerMove(actualPitMove);
		if (isFirstPlayerMovingNow != isFirstPlayerTurn(pitLastMove, stonesLastMove)) {
			log.info("It is not the player's turn. Last turn was {} with {} stones and new move is {}", pitLastMove, stonesLastMove,
					actualPitMove);
			throw new ValidationException("It is not the turn for moving that pit");
		}
	}

	/**
	 * Generates the new board after the movement:
	 * - Player skips opponent's house
	 * - Player ends in own empty pit, gets its stones and opponent's stones to its house.
	 * - If the next turn's player cannot move means the game finished and stones are moved to the proper houses.
	 *
	 * @param gameId
	 * @param pits          current board
	 * @param actualPitMove
	 * @return the new board
	 * @throws NotFoundException
	 */
	private int[] getNewPits(long gameId, int[] pits, int actualPitMove) throws NotFoundException {
		int stonesInPit = pits[actualPitMove];

		// Removing stones from first pit
		pits[actualPitMove] = 0;
		// Adding stones to next pits
		int currentPit;
		int lastPit = -1;
		int skippingHouse = 0;
		for (int i = 0; i < stonesInPit; i++) {
			currentPit = getNextPit(actualPitMove, actualPitMove + i + skippingHouse);
			// In case we skipped an opponent's house
			if (currentPit == lastPit) {
				skippingHouse++;
				currentPit = getNextPit(actualPitMove, actualPitMove + i + skippingHouse);
			}
			pits[currentPit]++;
			lastPit = currentPit;
		}

		int latestPit = getNextPit(actualPitMove, actualPitMove, stonesInPit + skippingHouse);
		if (hasPlayerFinishedInItsEmptyPit(latestPit, actualPitMove, pits)) {
			// Move player pit stones to house and opponent stones
			int opponentPit = getOpponentPit(latestPit);
			pits[getPlayerHouse(actualPitMove)] += pits[latestPit] + pits[opponentPit];
			pits[latestPit] = 0;
			pits[opponentPit] = 0;
		}

		if (isGameFinished(actualPitMove, stonesInPit, pits)) {
			moveAllStonesToHouse(pits);
			gameService.finish(gameId, pits[KalahConstants.PLAYER_ONE_HOUSE], pits[KalahConstants.PLAYER_TWO_HOUSE]);
		}

		boardService.addBoardToGame(gameId, actualPitMove, stonesInPit, pits);
		log.info("Successfully move pit {} for game {}", actualPitMove, gameId);
		return pits;
	}

	/**
	 * Checks whether the player has finished in an empty pit of its own
	 *
	 * @param latestPit
	 * @param actualPitMove
	 * @param pits
	 * @return if the player finished adding stones to its own empty pit
	 */
	private boolean hasPlayerFinishedInItsEmptyPit(int latestPit, int actualPitMove, int[] pits) {
		return getPlayerPits(actualPitMove).contains(latestPit) && pits[latestPit] == 1;
	}

	/**
	 * Checks if the next player has no more pits to move so the game is finished
	 *
	 * @param actualPitMove
	 * @param stonesInPit
	 * @param pits
	 * @return whether the game is finished
	 */
	private boolean isGameFinished(int actualPitMove, int stonesInPit, int[] pits) {
		boolean isFirstPlayerNextTurn = isFirstPlayerTurn(actualPitMove, stonesInPit);
		return arePlayerPitsEmpty(isFirstPlayerNextTurn, pits);
	}

	/**
	 * It moves all the stones in a player's pits to its own house. For both players.
	 *
	 * @param pits board
	 */
	private void moveAllStonesToHouse(int[] pits) {
		IntStream.range(0, KalahConstants.PLAYER_ONE_PIT_MOVES.size()).forEach(i -> {
			pits[KalahConstants.PLAYER_ONE_HOUSE] += pits[KalahConstants.PLAYER_ONE_PIT_MOVES.get(i)];
			pits[KalahConstants.PLAYER_ONE_PIT_MOVES.get(i)] = 0;
			pits[KalahConstants.PLAYER_TWO_HOUSE] += pits[KalahConstants.PLAYER_TWO_PIT_MOVES.get(i)];
			pits[KalahConstants.PLAYER_TWO_PIT_MOVES.get(i)] = 0;
		});
	}

	/**
	 * Returns whether the player's pits are empty
	 *
	 * @param isFirstPlayer
	 * @param pits
	 * @return flag whether pits have no stone for given player
	 */
	private boolean arePlayerPitsEmpty(boolean isFirstPlayer, int[] pits) {
		List<Integer> playerPits = getPlayerPits(isFirstPlayer);
		for (Integer currentPit : playerPits) {
			if (pits[currentPit] > 0)
				return false;
		}
		return true;
	}

	/**
	 * In a board, the player's pit faces opponent's pit.
	 * 1 -- 13, 2 -- 12, 3 -- 11, 4 -- 10, 5 -- 9, 6 -- 8
	 *
	 * @param playerPit
	 * @return the opponent's pit
	 */
	private int getOpponentPit(int playerPit) {
		return KalahConstants.VALID_PIT_MOVEMENTS.size() - playerPit;
	}

	/**
	 * Gets the next pit after one stone move. It skips the opponent's house.
	 *
	 * @param initialPit
	 * @param currentPit
	 * @return next pit
	 */
	private int getNextPit(int initialPit, int currentPit) {
		return getNextPit(initialPit, currentPit, 1);
	}

	/**
	 * Gets the next pit after one stone move. It skips the opponent's house.
	 *
	 * @param initialPit
	 * @param currentPit
	 * @param stones     number of pits to jump
	 * @return next pit
	 */
	private int getNextPit(int initialPit, int currentPit, int stones) {
		int nextPit = (currentPit + stones) % KalahConstants.BOARD_SIZE;
		boolean isFirstPlayerMove = isFirstPlayerMove(initialPit);
		if (isFirstPlayerMove && KalahConstants.PLAYER_TWO_HOUSE == nextPit) {
			nextPit = 0;
		} else if (!isFirstPlayerMove && KalahConstants.PLAYER_ONE_HOUSE == nextPit) {
			nextPit++;
		}
		return nextPit;
	}

	/**
	 * Returns whether the movement is the first in the board
	 *
	 * @param currentBoard
	 * @return boolean
	 */
	private boolean isFirstMove(Board currentBoard) {
		return null == currentBoard.getPitLastMove();
	}

	/**
	 * Gets whether the pit belongs to player 1 --> true
	 *
	 * @param pit from player
	 * @return boolean
	 */
	private boolean isFirstPlayerMove(int pit) {
		return KalahConstants.PLAYER_ONE_PIT_MOVES.contains(pit);
	}

	/**
	 * Given the pit, it identifies the player and returns player's house
	 *
	 * @param pit from player
	 * @return player's house position
	 */
	private int getPlayerHouse(int pit) {
		return isFirstPlayerMove(pit) ? KalahConstants.PLAYER_ONE_HOUSE : KalahConstants.PLAYER_TWO_HOUSE;
	}

	/**
	 * Given the player it returns the associated pits
	 *
	 * @param pit from player
	 * @return list of player's pits
	 */
	private List<Integer> getPlayerPits(int pit) {
		return getPlayerPits(isFirstPlayerMove(pit));
	}

	/**
	 * Given the player it returns the associated pits
	 *
	 * @param isFirstPlayer to identify player 1 or 2
	 * @return list of player's pits
	 */
	private List<Integer> getPlayerPits(boolean isFirstPlayer) {
		return isFirstPlayer ? KalahConstants.PLAYER_ONE_PIT_MOVES : KalahConstants.PLAYER_TWO_PIT_MOVES;
	}

	/**
	 * Returns whether the next turn is for the first player or not.
	 *
	 * @param lastPitMoved   last pit selected to empty
	 * @param stonesLastMove stones in the pit from last movement
	 * @return if next turn is first player
	 */
	private boolean isFirstPlayerTurn(int lastPitMoved, int stonesLastMove) {
		int lastStonePit = getNextPit(lastPitMoved, lastPitMoved, stonesLastMove);
		int lastPlayerHouse = getPlayerHouse(lastPitMoved);
		boolean finishedInHouse = lastPlayerHouse == lastStonePit;
		boolean wasFirstPlayerMove = isFirstPlayerMove(lastPitMoved);
		return !wasFirstPlayerMove && !finishedInHouse || wasFirstPlayerMove && finishedInHouse;
	}
}
