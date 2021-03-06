package org.juanjo.kalah.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.juanjo.kalah.dao.Board;
import org.juanjo.kalah.dto.GameDTO;

public class TestUtils {
	public static Board getRandomBoard() {
		Board board = new Board();
		board.setGameId(RandomUtils.nextLong(1, 99999));
		board.setPits(new int[] {6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0});
		return board;
	}

	public static GameDTO getRandomGameDTO() {
		GameDTO game = new GameDTO();
		game.setId(RandomUtils.nextLong());
		game.setUrl(RandomStringUtils.randomAlphanumeric(32));
		return game;
	}
}
