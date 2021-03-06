package org.juanjo.kalah.controller;

import org.juanjo.kalah.dto.GameStatusDTO;
import org.juanjo.kalah.exception.ValidationException;
import org.juanjo.kalah.service.PitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller to handle the pit moves
 */
@RestController
@RequestMapping(value = "/games/{gameId}/pits/{pitId}", produces = MediaType.APPLICATION_JSON_VALUE)
public class PitController {
	private final PitService pitService;

	@Autowired
	public PitController(PitService pitService) {
		this.pitService = pitService;
	}

	/**
	 * Performs the movement
	 *
	 * @param gameId to play
	 * @param pitId  to move
	 * @return the game board after the move
	 * @throws ValidationException in case the movement is invalid
	 */
	@PutMapping
	public GameStatusDTO move(@PathVariable long gameId, @PathVariable int pitId) throws ValidationException {
		return pitService.move(gameId, pitId);
	}
}
