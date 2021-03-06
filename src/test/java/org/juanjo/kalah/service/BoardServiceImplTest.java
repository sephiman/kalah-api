package org.juanjo.kalah.service;

import org.apache.commons.lang3.RandomUtils;
import org.juanjo.kalah.dao.Board;
import org.juanjo.kalah.persistence.BoardRepository;
import org.juanjo.kalah.utils.KalahConstants;
import org.juanjo.kalah.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class BoardServiceImplTest {
	private BoardServiceImpl service;
	@Mock
	private BoardRepository repository;
	private static final int INITIAL_STONES = 6;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		service = new BoardServiceImpl(repository, INITIAL_STONES);
	}

	@Test
	public void testInitializeBoard() {
		long gameId = RandomUtils.nextLong();
		service.initializeBoard(gameId);
		ArgumentCaptor<Board> captor = ArgumentCaptor.forClass(Board.class);
		verify(repository).save(captor.capture());
		Board inserted = captor.getValue();
		assertNotNull(inserted);
		assertEquals(gameId, inserted.getGameId());
		IntStream.range(0, KalahConstants.BOARD_SIZE).forEach(i -> {
			if (KalahConstants.VALID_PIT_MOVEMENTS.contains(i)) {
				assertEquals(INITIAL_STONES, inserted.getPits()[i]);
			} else {
				assertEquals(KalahConstants.INITIAL_HOUSE_STONES, inserted.getPits()[i]);
			}
		});
	}

	@Test
	public void testGetLatestBoard() {
		long gameId = RandomUtils.nextLong();
		Board board = TestUtils.getRandomBoard();
		when(repository.findFirstByGameIdOrderByCreateDateDesc(gameId)).thenReturn(board);
		Board result = service.getLatestBoard(gameId);
		assertNotNull(result);
	}

	@Test
	public void testAddBoardToGame() {
		long gameId = RandomUtils.nextLong();
		int lastMovedPit = RandomUtils.nextInt();
		int stonesBeforeMovingPit = RandomUtils.nextInt();
		int[] pits = new int[14];
		service.addBoardToGame(gameId, lastMovedPit, stonesBeforeMovingPit, pits);
		ArgumentCaptor<Board> captor = ArgumentCaptor.forClass(Board.class);
		verify(repository).save(captor.capture());
		Board inserted = captor.getValue();
		assertNotNull(inserted);
		assertEquals(gameId, inserted.getGameId());
		assertEquals(pits, inserted.getPits());
		assertEquals(lastMovedPit, inserted.getPitLastMove());
		assertEquals(stonesBeforeMovingPit, inserted.getStonesLastMove());
	}
}
