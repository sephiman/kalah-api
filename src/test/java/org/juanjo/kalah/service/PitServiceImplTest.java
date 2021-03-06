package org.juanjo.kalah.service;

import org.apache.commons.lang3.RandomUtils;
import org.juanjo.kalah.dao.Board;
import org.juanjo.kalah.dto.GameDTO;
import org.juanjo.kalah.dto.GameStatusDTO;
import org.juanjo.kalah.exception.ValidationException;
import org.juanjo.kalah.utils.KalahConstants;
import org.juanjo.kalah.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class PitServiceImplTest {
	@InjectMocks
	private PitServiceImpl service;
	@Mock
	private GameService gameService;
	@Mock
	private BoardService boardService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testMoveInvalidPitLessThanMin() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 0;
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		assertThrows(ValidationException.class, () -> service.move(gameId, pitId));
		verify(boardService, never()).addBoardToGame(anyLong(), anyInt(), anyInt(), any());
	}

	@Test
	public void testMoveInvalidPitMoreThanMax() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = KalahConstants.BOARD_SIZE + 1;
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		assertThrows(ValidationException.class, () -> service.move(gameId, pitId));
		verify(boardService, never()).addBoardToGame(anyLong(), anyInt(), anyInt(), any());
	}

	@Test
	public void testMoveInvalidPitHouseMovementP1() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = KalahConstants.PLAYER_ONE_HOUSE + 1;
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		assertThrows(ValidationException.class, () -> service.move(gameId, pitId));
		verify(boardService, never()).addBoardToGame(anyLong(), anyInt(), anyInt(), any());
	}

	@Test
	public void testMoveInvalidPitHouseMovementP2() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = KalahConstants.PLAYER_TWO_HOUSE + 1;
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		assertThrows(ValidationException.class, () -> service.move(gameId, pitId));
		verify(boardService, never()).addBoardToGame(anyLong(), anyInt(), anyInt(), any());
	}

	@Test
	public void testMoveInvalidPitEmptyP1() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 1;
		int[] currentBoard = {0, 6, 6, 6, 6, 6, 2, 1, 6, 6, 6, 6, 6, 2};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(RandomUtils.nextInt());
		board.setStonesLastMove(RandomUtils.nextInt());
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		assertThrows(ValidationException.class, () -> service.move(gameId, pitId));
		verify(boardService, never()).addBoardToGame(anyLong(), anyInt(), anyInt(), any());
	}

	@Test
	public void testMoveInvalidPitEmptyP2() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 13;
		int[] currentBoard = {0, 6, 6, 6, 6, 6, 2, 1, 6, 6, 6, 6, 0, 2};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(RandomUtils.nextInt());
		board.setStonesLastMove(RandomUtils.nextInt());
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		assertThrows(ValidationException.class, () -> service.move(gameId, pitId));
		verify(boardService, never()).addBoardToGame(anyLong(), anyInt(), anyInt(), any());
	}

	@Test
	public void testMoveInvalidP1WhenP2TurnNoHouse() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 2;
		int[] currentBoard = {0, 6, 6, 6, 6, 6, 2, 1, 6, 6, 6, 6, 0, 2};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(2);
		board.setStonesLastMove(2);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		assertThrows(ValidationException.class, () -> service.move(gameId, pitId));
		verify(boardService, never()).addBoardToGame(anyLong(), anyInt(), anyInt(), any());
	}

	@Test
	public void testMoveInvalidP1WhenP2FinishedInHouse() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 2;
		int[] currentBoard = {0, 6, 6, 6, 6, 6, 2, 1, 6, 6, 6, 6, 0, 2};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(7);
		board.setStonesLastMove(6);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		assertThrows(ValidationException.class, () -> service.move(gameId, pitId));
		verify(boardService, never()).addBoardToGame(anyLong(), anyInt(), anyInt(), any());
	}

	@Test
	public void testMoveInvalidP2WhenP1TurnNoHouse() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 10;
		int[] currentBoard = {0, 6, 6, 6, 6, 6, 2, 1, 6, 6, 6, 6, 0, 2};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(9);
		board.setStonesLastMove(3);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		assertThrows(ValidationException.class, () -> service.move(gameId, pitId));
		verify(boardService, never()).addBoardToGame(anyLong(), anyInt(), anyInt(), any());
	}

	@Test
	public void testMoveInvalidP2WhenP1FinishedInHouse() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 10;
		int[] currentBoard = {0, 6, 6, 6, 6, 6, 2, 1, 6, 6, 6, 6, 0, 2};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(5);
		board.setStonesLastMove(1);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		assertThrows(ValidationException.class, () -> service.move(gameId, pitId));
		verify(boardService, never()).addBoardToGame(anyLong(), anyInt(), anyInt(), any());
	}

	@Test
	public void testMoveFirstMoveP1() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 2;
		int[] currentBoard = {6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(null);
		board.setStonesLastMove(null);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		Board newBoard = TestUtils.getRandomBoard();
		ArgumentCaptor<int[]> captor = ArgumentCaptor.forClass(int[].class);
		when(boardService.addBoardToGame(eq(gameId), eq(pitId - 1), eq(6), captor.capture())).thenReturn(newBoard);
		GameStatusDTO result = service.move(gameId, pitId);
		assertNotNull(result);
		int[] boardAfterMove = captor.getValue();
		int[] expectedBoard = {6, 0, 7, 7, 7, 7, 1, 7, 6, 6, 6, 6, 6, 0};
		IntStream.range(0, KalahConstants.BOARD_SIZE)
				.forEach(i -> assertEquals(expectedBoard[i], boardAfterMove[i], "Failed checking pit " + i));
		verify(gameService, never()).finish(anyLong(), anyInt(), anyInt());
	}

	@Test
	public void testMoveFirstMoveP2() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 12;
		int[] currentBoard = {6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(null);
		board.setStonesLastMove(null);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		Board newBoard = TestUtils.getRandomBoard();
		ArgumentCaptor<int[]> captor = ArgumentCaptor.forClass(int[].class);
		when(boardService.addBoardToGame(eq(gameId), eq(pitId - 1), eq(6), captor.capture())).thenReturn(newBoard);
		GameStatusDTO result = service.move(gameId, pitId);
		assertNotNull(result);
		int[] boardAfterMove = captor.getValue();
		int[] expectedBoard = {7, 7, 7, 7, 6, 6, 0, 6, 6, 6, 6, 0, 7, 1};
		IntStream.range(0, KalahConstants.BOARD_SIZE)
				.forEach(i -> assertEquals(expectedBoard[i], boardAfterMove[i], "Failed checking pit " + i));
		verify(gameService, never()).finish(anyLong(), anyInt(), anyInt());
	}

	@Test
	public void testMoveFirstMoveP1FinishingHouse() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 1;
		int[] currentBoard = {6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(null);
		board.setStonesLastMove(null);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		Board newBoard = TestUtils.getRandomBoard();
		ArgumentCaptor<int[]> captor = ArgumentCaptor.forClass(int[].class);
		when(boardService.addBoardToGame(eq(gameId), eq(0), eq(6), captor.capture())).thenReturn(newBoard);
		GameStatusDTO result = service.move(gameId, pitId);
		assertNotNull(result);
		int[] boardAfterMove = captor.getValue();
		int[] expectedBoard = {0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0};
		IntStream.range(0, KalahConstants.BOARD_SIZE)
				.forEach(i -> assertEquals(expectedBoard[i], boardAfterMove[i], "Failed checking pit " + i));
		verify(gameService, never()).finish(anyLong(), anyInt(), anyInt());
	}

	@Test
	public void testMoveFirstMoveP2FinishingHouse() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 8;
		int[] currentBoard = {6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(null);
		board.setStonesLastMove(null);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		Board newBoard = TestUtils.getRandomBoard();
		ArgumentCaptor<int[]> captor = ArgumentCaptor.forClass(int[].class);
		when(boardService.addBoardToGame(eq(gameId), eq(pitId - 1), eq(6), captor.capture())).thenReturn(newBoard);
		GameStatusDTO result = service.move(gameId, pitId);
		assertNotNull(result);
		int[] boardAfterMove = captor.getValue();
		int[] expectedBoard = {6, 6, 6, 6, 6, 6, 0, 0, 7, 7, 7, 7, 7, 1};
		IntStream.range(0, KalahConstants.BOARD_SIZE)
				.forEach(i -> assertEquals(expectedBoard[i], boardAfterMove[i], "Failed checking pit " + i));
		verify(gameService, never()).finish(anyLong(), anyInt(), anyInt());
	}

	@Test
	public void testMoveP1MovesSkippingP2House() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 6;
		int[] currentBoard = {6, 6, 6, 6, 6, 10, 0, 6, 6, 6, 6, 6, 6, 0};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(null);
		board.setStonesLastMove(null);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		Board newBoard = TestUtils.getRandomBoard();
		ArgumentCaptor<int[]> captor = ArgumentCaptor.forClass(int[].class);
		when(boardService.addBoardToGame(eq(gameId), eq(pitId - 1), eq(10), captor.capture())).thenReturn(newBoard);
		GameStatusDTO result = service.move(gameId, pitId);
		assertNotNull(result);
		int[] boardAfterMove = captor.getValue();
		int[] expectedBoard = {7, 7, 7, 6, 6, 0, 1, 7, 7, 7, 7, 7, 7, 0};
		IntStream.range(0, KalahConstants.BOARD_SIZE)
				.forEach(i -> assertEquals(expectedBoard[i], boardAfterMove[i], "Failed checking pit " + i));
		verify(gameService, never()).finish(anyLong(), anyInt(), anyInt());
	}

	@Test
	public void testMoveP2MovesSkippingP1House() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 13;
		int[] currentBoard = {6, 6, 6, 6, 6, 10, 0, 6, 6, 6, 6, 6, 10, 0};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(null);
		board.setStonesLastMove(null);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		Board newBoard = TestUtils.getRandomBoard();
		ArgumentCaptor<int[]> captor = ArgumentCaptor.forClass(int[].class);
		when(boardService.addBoardToGame(eq(gameId), eq(pitId - 1), eq(10), captor.capture())).thenReturn(newBoard);
		GameStatusDTO result = service.move(gameId, pitId);
		assertNotNull(result);
		int[] boardAfterMove = captor.getValue();
		int[] expectedBoard = {7, 7, 7, 7, 7, 11, 0, 7, 7, 7, 6, 6, 0, 1};
		IntStream.range(0, KalahConstants.BOARD_SIZE)
				.forEach(i -> assertEquals(expectedBoard[i], boardAfterMove[i], "Failed checking pit " + i));
		verify(gameService, never()).finish(anyLong(), anyInt(), anyInt());
	}

	@Test
	public void testMoveP1MovesIntoItsEmptyPitAndP2IsEmptyToo() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 2;
		int[] currentBoard = {6, 3, 6, 1, 0, 10, 0, 6, 0, 6, 6, 0, 10, 1};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(null);
		board.setStonesLastMove(null);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		Board newBoard = TestUtils.getRandomBoard();
		ArgumentCaptor<int[]> captor = ArgumentCaptor.forClass(int[].class);
		when(boardService.addBoardToGame(eq(gameId), eq(pitId - 1), eq(3), captor.capture())).thenReturn(newBoard);
		GameStatusDTO result = service.move(gameId, pitId);
		assertNotNull(result);
		int[] boardAfterMove = captor.getValue();
		int[] expectedBoard = {6, 0, 7, 2, 0, 10, 1, 6, 0, 6, 6, 0, 10, 1};
		IntStream.range(0, KalahConstants.BOARD_SIZE)
				.forEach(i -> assertEquals(expectedBoard[i], boardAfterMove[i], "Failed checking pit " + i));
		verify(gameService, never()).finish(anyLong(), anyInt(), anyInt());
	}

	@Test
	public void testMoveP2MovesIntoItsEmptyPitAndP1IsEmptyToo() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 9;
		int[] currentBoard = {0, 3, 6, 1, 6, 2, 3, 6, 4, 6, 6, 0, 0, 1};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(null);
		board.setStonesLastMove(null);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		Board newBoard = TestUtils.getRandomBoard();
		ArgumentCaptor<int[]> captor = ArgumentCaptor.forClass(int[].class);
		when(boardService.addBoardToGame(eq(gameId), eq(pitId - 1), eq(4), captor.capture())).thenReturn(newBoard);
		GameStatusDTO result = service.move(gameId, pitId);
		assertNotNull(result);
		int[] boardAfterMove = captor.getValue();
		int[] expectedBoard = {0, 3, 6, 1, 6, 2, 3, 6, 0, 7, 7, 1, 0, 2};
		IntStream.range(0, KalahConstants.BOARD_SIZE)
				.forEach(i -> assertEquals(expectedBoard[i], boardAfterMove[i], "Failed checking pit " + i));
		verify(gameService, never()).finish(anyLong(), anyInt(), anyInt());
	}

	@Test
	public void testMoveP1MovesIntoItsEmptyPitAndP2HasStones() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 2;
		int[] currentBoard = {6, 3, 6, 1, 0, 10, 0, 6, 4, 6, 6, 0, 10, 1};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(null);
		board.setStonesLastMove(null);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		Board newBoard = TestUtils.getRandomBoard();
		ArgumentCaptor<int[]> captor = ArgumentCaptor.forClass(int[].class);
		when(boardService.addBoardToGame(eq(gameId), eq(pitId - 1), eq(3), captor.capture())).thenReturn(newBoard);
		GameStatusDTO result = service.move(gameId, pitId);
		assertNotNull(result);
		int[] boardAfterMove = captor.getValue();
		int[] expectedBoard = {6, 0, 7, 2, 0, 10, 5, 6, 0, 6, 6, 0, 10, 1};
		IntStream.range(0, KalahConstants.BOARD_SIZE)
				.forEach(i -> assertEquals(expectedBoard[i], boardAfterMove[i], "Failed checking pit " + i));
		verify(gameService, never()).finish(anyLong(), anyInt(), anyInt());
	}

	@Test
	public void testMoveP2MovesIntoItsEmptyPitAndP1hasStones() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 9;
		int[] currentBoard = {10, 3, 6, 1, 6, 2, 3, 6, 4, 6, 6, 0, 0, 1};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(null);
		board.setStonesLastMove(null);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		Board newBoard = TestUtils.getRandomBoard();
		ArgumentCaptor<int[]> captor = ArgumentCaptor.forClass(int[].class);
		when(boardService.addBoardToGame(eq(gameId), eq(pitId - 1), eq(4), captor.capture())).thenReturn(newBoard);
		GameStatusDTO result = service.move(gameId, pitId);
		assertNotNull(result);
		int[] boardAfterMove = captor.getValue();
		int[] expectedBoard = {0, 3, 6, 1, 6, 2, 3, 6, 0, 7, 7, 1, 0, 12};
		IntStream.range(0, KalahConstants.BOARD_SIZE)
				.forEach(i -> assertEquals(expectedBoard[i], boardAfterMove[i], "Failed checking pit " + i));
		verify(gameService, never()).finish(anyLong(), anyInt(), anyInt());
	}

	@Test
	public void testMoveP1MovesAndGameFinishedP2CannotMove() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 4;
		int[] currentBoard = {6, 3, 6, 1, 0, 10, 14, 0, 0, 0, 0, 0, 0, 22};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(null);
		board.setStonesLastMove(null);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		Board newBoard = TestUtils.getRandomBoard();
		ArgumentCaptor<int[]> captor = ArgumentCaptor.forClass(int[].class);
		when(boardService.addBoardToGame(eq(gameId), eq(pitId - 1), eq(1), captor.capture())).thenReturn(newBoard);
		GameStatusDTO result = service.move(gameId, pitId);
		assertNotNull(result);
		int[] boardAfterMove = captor.getValue();
		int[] expectedBoard = {0, 0, 0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 22};
		IntStream.range(0, KalahConstants.BOARD_SIZE)
				.forEach(i -> assertEquals(expectedBoard[i], boardAfterMove[i], "Failed checking pit " + i));
		verify(gameService).finish(gameId, 40, 22);
	}

	@Test
	public void testMoveP2MovesAndGameFinishedP1CannotMove() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 11;
		int[] currentBoard = {0, 5, 0, 0, 0, 0, 22, 6, 3, 6, 1, 0, 10, 14};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(null);
		board.setStonesLastMove(null);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		Board newBoard = TestUtils.getRandomBoard();
		ArgumentCaptor<int[]> captor = ArgumentCaptor.forClass(int[].class);
		when(boardService.addBoardToGame(eq(gameId), eq(pitId - 1), eq(1), captor.capture())).thenReturn(newBoard);
		GameStatusDTO result = service.move(gameId, pitId);
		assertNotNull(result);
		int[] boardAfterMove = captor.getValue();
		int[] expectedBoard = {0, 0, 0, 0, 0, 0, 22, 0, 0, 0, 0, 0, 0, 45};
		IntStream.range(0, KalahConstants.BOARD_SIZE)
				.forEach(i -> assertEquals(expectedBoard[i], boardAfterMove[i], "Failed checking pit " + i));
		verify(gameService).finish(gameId, 22, 45);
	}

	@Test
	public void testMoveP1MovesAndGameFinishedP1CannotMove() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 6;
		int[] currentBoard = {0, 0, 0, 0, 0, 1, 14, 0, 5, 4, 0, 2, 3, 22};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(null);
		board.setStonesLastMove(null);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		Board newBoard = TestUtils.getRandomBoard();
		ArgumentCaptor<int[]> captor = ArgumentCaptor.forClass(int[].class);
		when(boardService.addBoardToGame(eq(gameId), eq(pitId - 1), eq(1), captor.capture())).thenReturn(newBoard);
		GameStatusDTO result = service.move(gameId, pitId);
		assertNotNull(result);
		int[] boardAfterMove = captor.getValue();
		int[] expectedBoard = {0, 0, 0, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0, 36};
		IntStream.range(0, KalahConstants.BOARD_SIZE)
				.forEach(i -> assertEquals(expectedBoard[i], boardAfterMove[i], "Failed checking pit " + i));
		verify(gameService).finish(gameId, 15, 36);
	}

	@Test
	public void testMoveP2MovesAndGameFinishedP2CannotMove() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = 13;
		int[] currentBoard = {0, 5, 4, 0, 2, 3, 22, 0, 0, 0, 0, 0, 1, 14};
		GameDTO game = TestUtils.getRandomGameDTO();
		when(gameService.getNonFinishedById(gameId)).thenReturn(game);
		Board board = TestUtils.getRandomBoard();
		board.setPits(currentBoard);
		board.setPitLastMove(null);
		board.setStonesLastMove(null);
		when(boardService.getLatestBoard(gameId)).thenReturn(board);
		Board newBoard = TestUtils.getRandomBoard();
		ArgumentCaptor<int[]> captor = ArgumentCaptor.forClass(int[].class);
		when(boardService.addBoardToGame(eq(gameId), eq(pitId - 1), eq(1), captor.capture())).thenReturn(newBoard);
		GameStatusDTO result = service.move(gameId, pitId);
		assertNotNull(result);
		int[] boardAfterMove = captor.getValue();
		int[] expectedBoard = {0, 0, 0, 0, 0, 0, 36, 0, 0, 0, 0, 0, 0, 15};
		IntStream.range(0, KalahConstants.BOARD_SIZE)
				.forEach(i -> assertEquals(expectedBoard[i], boardAfterMove[i], "Failed checking pit " + i));
		verify(gameService).finish(gameId, 36, 15);
	}
}
