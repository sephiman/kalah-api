package org.juanjo.kalah.utils;

import org.juanjo.kalah.dao.Game;
import org.juanjo.kalah.dto.GameDTO;
import org.juanjo.kalah.dto.GameStatusDTO;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Mapper for game dto
 */
public class GameMapper {

	/**
	 * Generates a new GameDto given the persistence object and url
	 *
	 * @param game from database
	 * @param url  for the game
	 * @return game dto
	 */
	public static GameDTO fromPersistence(Game game, String url) {
		return new GameDTO(game.getId(), url);
	}

	/**
	 * Generates a new gameDTO including the status of the board
	 *
	 * @param gameId associated
	 * @param url    for the game
	 * @param pits   current board
	 * @return game dto with board status
	 */
	public static GameStatusDTO getGameStatus(long gameId, String url, int[] pits) {
		GameStatusDTO result = new GameStatusDTO();
		result.setId(gameId);
		result.setUrl(url);
		result.setStatus(IntStream.range(0, pits.length).boxed().collect(Collectors.toMap(i -> ++i, i -> String.valueOf(pits[i]))));
		return result;
	}
}
