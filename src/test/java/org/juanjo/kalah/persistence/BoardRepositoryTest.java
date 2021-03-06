package org.juanjo.kalah.persistence;

import org.apache.commons.lang3.RandomUtils;
import org.juanjo.kalah.dao.Board;
import org.juanjo.kalah.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:application-test.yml")
@DataJpaTest
public class BoardRepositoryTest {
	@Autowired
	private BoardRepository repository;

	@Test
	void testSave() {
		Board board = TestUtils.getRandomBoard();
		Board inserted = repository.save(board);
		assertNotNull(inserted.getId());
		assertNotNull(inserted.getCreateDate());
		assertNotNull(inserted.getGameId());
		assertNotNull(inserted.getPits());
		assertEquals(board.getPits().length, inserted.getPits().length);
		assertNull(inserted.getPitLastMove());
		assertNull(inserted.getStonesLastMove());
		inserted.setStonesLastMove(RandomUtils.nextInt());
		inserted.setPitLastMove(RandomUtils.nextInt());
		repository.save(inserted);
		Optional<Board> updated = repository.findById(inserted.getId());
		assertTrue(updated.isPresent());
		assertEquals(inserted.getPitLastMove(), updated.get().getPitLastMove());
		assertEquals(inserted.getStonesLastMove(), updated.get().getStonesLastMove());
	}

	@Test
	void testFindFirstByGameIdOrderByCreateDateDesc() throws InterruptedException {
		Board result = repository.findFirstByGameIdOrderByCreateDateDesc(RandomUtils.nextLong());
		assertNull(result);
		Board board1 = TestUtils.getRandomBoard();
		repository.save(board1);
		Board board2 = TestUtils.getRandomBoard();
		board2.setGameId(board1.getGameId());
		Thread.sleep(100);
		repository.save(board2);
		Board board3 = TestUtils.getRandomBoard();
		board3.setGameId(board1.getGameId());
		Thread.sleep(100);
		repository.save(board3);
		result = repository.findFirstByGameIdOrderByCreateDateDesc(board1.getGameId());
		assertNotNull(result);
		assertEquals(board3.getId(), result.getId());
		assertEquals(board3.getGameId(), result.getGameId());
		assertEquals(board3.getPits().length, result.getPits().length);
		assertEquals(board3.getCreateDate(), result.getCreateDate());
	}
}
