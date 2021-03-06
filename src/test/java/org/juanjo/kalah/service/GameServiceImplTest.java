package org.juanjo.kalah.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.juanjo.kalah.dao.Game;
import org.juanjo.kalah.dto.GameDTO;
import org.juanjo.kalah.dto.GameStatus;
import org.juanjo.kalah.exception.NotFoundException;
import org.juanjo.kalah.exception.ValidationException;
import org.juanjo.kalah.persistence.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class GameServiceImplTest {
	@InjectMocks
	private GameServiceImpl service;
	@Mock
	private GameRepository repository;
	@Mock
	private BoardService boardService;
	@Mock
	private UrlService urlService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateKalahGame() {
		Game game = new Game();
		game.setId(RandomUtils.nextLong());
		when(repository.save(any())).thenReturn(game);
		String url = RandomStringUtils.randomAlphanumeric(32);
		when(urlService.getUrlForGame(game.getId())).thenReturn(url);
		GameDTO result = service.createKalahGame();
		assertNotNull(result);
		assertEquals(url, result.getUrl());
		assertEquals(game.getId(), result.getId());
		verify(boardService).initializeBoard(game.getId());
	}

	@Test
	public void testGetNonFinishedByIdKoNotFound() {
		long gameId = RandomUtils.nextLong();
		assertThrows(NotFoundException.class, () -> service.getNonFinishedById(gameId));
	}

	@Test
	public void testGetNonFinishedByIdKoFinished() {
		long gameId = RandomUtils.nextLong();
		Game game = new Game();
		game.setStatus(GameStatus.FINISHED);
		when(repository.findById(gameId)).thenReturn(of(game));
		assertThrows(ValidationException.class, () -> service.getNonFinishedById(gameId));
	}

	@Test
	public void testGetNonFinishedByIdOk() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		Game game = new Game();
		game.setId(gameId);
		game.setStatus(GameStatus.IN_PROGRESS);
		when(repository.findById(gameId)).thenReturn(of(game));
		String url = RandomStringUtils.randomAlphanumeric(32);
		when(urlService.getUrlForGame(game.getId())).thenReturn(url);
		GameDTO result = service.getNonFinishedById(gameId);
		assertNotNull(result);
		assertEquals(url, result.getUrl());
		assertEquals(gameId, result.getId());
	}

	@Test
	public void testFinishKoNotFound() {
		long gameId = RandomUtils.nextLong();
		int firstPlayerScore = RandomUtils.nextInt();
		int secondPlayerScore = RandomUtils.nextInt();
		assertThrows(NotFoundException.class, () -> service.finish(gameId, firstPlayerScore, secondPlayerScore));
	}

	@Test
	public void testFinishOk() throws NotFoundException {
		long gameId = RandomUtils.nextLong();
		int firstPlayerScore = RandomUtils.nextInt();
		int secondPlayerScore = RandomUtils.nextInt();
		when(repository.findById(gameId)).thenReturn(of(new Game()));
		ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
		service.finish(gameId, firstPlayerScore, secondPlayerScore);
		verify(repository).save(captor.capture());
		Game updated = captor.getValue();
		assertNotNull(updated);
		assertEquals(GameStatus.FINISHED, updated.getStatus());
		assertEquals(firstPlayerScore, updated.getFirstPlayerScore());
		assertEquals(secondPlayerScore, updated.getSecondPlayerScore());
	}
}
