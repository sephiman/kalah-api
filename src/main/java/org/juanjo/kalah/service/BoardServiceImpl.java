package org.juanjo.kalah.service;

import lombok.extern.slf4j.Slf4j;
import org.juanjo.kalah.dao.Board;
import org.juanjo.kalah.persistence.BoardRepository;
import org.juanjo.kalah.utils.KalahConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BoardServiceImpl implements BoardService {
	private final BoardRepository repository;
	private final int initialStones;

	@Autowired
	public BoardServiceImpl(BoardRepository repository, @Value("${kalah.config.stones}") int initialStones) {
		this.repository = repository;
		this.initialStones = initialStones;
	}

	@Override
	public void initializeBoard(long gameId) {
		Board board = new Board();
		board.setGameId(gameId);
		int[] initialBoard = generateInitialBoard();

		board.setPits(initialBoard);
		repository.save(board);
		log.info("Board initialized for game {}", gameId);
	}

	/**
	 * Generates the initial board adding 0 stones to houses and the specified stones to player's pits
	 *
	 * @return new board
	 */
	private int[] generateInitialBoard() {
		return new int[] {initialStones, initialStones, initialStones, initialStones, initialStones, initialStones,
				KalahConstants.INITIAL_HOUSE_STONES, initialStones, initialStones, initialStones, initialStones, initialStones,
				initialStones, KalahConstants.INITIAL_HOUSE_STONES};
	}

	@Override
	public Board getLatestBoard(long gameId) {
		return repository.findFirstByGameIdOrderByCreateDateDesc(gameId);
	}

	@Override
	public Board addBoardToGame(long gameId, int lastMovedPit, int stonesBeforeMovingPit, int[] pits) {
		Board board = new Board();
		board.setGameId(gameId);
		board.setPitLastMove(lastMovedPit);
		board.setStonesLastMove(stonesBeforeMovingPit);
		board.setPits(pits);
		repository.save(board);
		log.info("Board created for game {}", gameId);
		return board;
	}
}
