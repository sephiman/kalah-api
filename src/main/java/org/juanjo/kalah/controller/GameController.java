package org.juanjo.kalah.controller;

import org.juanjo.kalah.dto.GameDTO;
import org.juanjo.kalah.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller to handle games
 */
@RestController
@RequestMapping(value = "/games", produces = MediaType.APPLICATION_JSON_VALUE)
public class GameController {
	private final GameService gameService;

	@Autowired
	public GameController(GameService gameService) {
		this.gameService = gameService;
	}

	/**
	 * Creates a new board for a kalah game
	 *
	 * @return created kalah game
	 */
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public GameDTO createKalahGame() {
		return gameService.createKalahGame();
	}
}
