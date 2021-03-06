package org.juanjo.kalah.persistence;

import org.apache.commons.lang3.RandomUtils;
import org.juanjo.kalah.dao.Game;
import org.juanjo.kalah.dto.GameStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:application-test.yml")
@DataJpaTest
public class GameRepositoryTest {
	@Autowired
	private GameRepository repository;

	@Test
	void testSave() throws InterruptedException {
		Game game = new Game();
		Game inserted = repository.save(game);
		assertNotNull(inserted.getId());
		assertNotNull(inserted.getCreateDate());
		assertEquals(GameStatus.IN_PROGRESS, inserted.getStatus());
		assertEquals(inserted.getCreateDate(), inserted.getUpdateDate());
		assertNull(inserted.getFirstPlayerScore());
		assertNull(inserted.getSecondPlayerScore());
		Thread.sleep(1000);
		inserted.setFirstPlayerScore(RandomUtils.nextInt());
		inserted.setSecondPlayerScore(RandomUtils.nextInt());
		inserted.setStatus(GameStatus.FINISHED);
		repository.flush();
		Game updated = repository.save(inserted);
		assertTrue(updated.getUpdateDate().isAfter(updated.getCreateDate()));
		assertEquals(GameStatus.FINISHED, updated.getStatus());
		assertEquals(inserted.getFirstPlayerScore(), updated.getFirstPlayerScore());
		assertEquals(inserted.getSecondPlayerScore(), updated.getSecondPlayerScore());
	}

	@Test
	void testFindById() {
		Game game = new Game();
		Game inserted = repository.save(game);
		Optional<Game> found = repository.findById(inserted.getId());
		if (found.isPresent()) {
			assertNotNull(found.get().getId());
			assertNotNull(found.get().getCreateDate());
		} else {
			fail("Not found");
		}
	}
}
