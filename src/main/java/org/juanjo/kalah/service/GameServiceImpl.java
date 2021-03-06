package org.juanjo.kalah.service;

import lombok.extern.slf4j.Slf4j;
import org.juanjo.kalah.dao.Game;
import org.juanjo.kalah.dto.GameDTO;
import org.juanjo.kalah.dto.GameStatus;
import org.juanjo.kalah.exception.NotFoundException;
import org.juanjo.kalah.exception.ValidationException;
import org.juanjo.kalah.persistence.GameRepository;
import org.juanjo.kalah.utils.GameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class GameServiceImpl implements GameService {
	private final GameRepository repository;
	private final UrlService urlService;
	private final BoardService boardService;

	@Autowired
	public GameServiceImpl(GameRepository repository, UrlService urlService, BoardService boardService) {
		this.repository = repository;
		this.urlService = urlService;
		this.boardService = boardService;
	}

	@Override
	@Transactional
	public GameDTO createKalahGame() {
		Game game = repository.save(new Game());
		log.info("Game id {} has been created", game.getId());
		boardService.initializeBoard(game.getId());
		return GameMapper.fromPersistence(game, urlService.getUrlForGame(game.getId()));
	}

	@Override
	public GameDTO getNonFinishedById(long gameId) throws ValidationException {
		Game game = repository.findById(gameId).orElseThrow(() -> {
			log.info("Game id {} not found", gameId);
			return new NotFoundException("Game not found");
		});
		if (GameStatus.FINISHED.equals(game.getStatus())) {
			throw new ValidationException("Game already finished");
		}
		return GameMapper.fromPersistence(game, urlService.getUrlForGame(game.getId()));
	}

	@Override
	public void finish(long gameId, int firstPlayerScore, int secondPlayerScore) throws NotFoundException {
		Game game = repository.findById(gameId).orElseThrow(() -> {
			log.info("Game id {} not found", gameId);
			return new NotFoundException("Game not found");
		});
		game.setStatus(GameStatus.FINISHED);
		game.setFirstPlayerScore(firstPlayerScore);
		game.setSecondPlayerScore(secondPlayerScore);
		repository.save(game);
		log.info("Game id {} has finished with score first player {} vs {} second player", gameId, firstPlayerScore, secondPlayerScore);
	}
}
