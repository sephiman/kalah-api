package org.juanjo.kalah.service;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class UrlServiceImplTest {
	private UrlServiceImpl service;
	private static final int PORT = 80;

	@BeforeEach
	public void setup() throws UnknownHostException {
		service = new UrlServiceImpl(PORT);
	}

	@Test
	public void testGetUrlForGameOk() {
		long gameId = RandomUtils.nextLong();
		String url = service.getUrlForGame(gameId);
		assertNotNull(url);
		assertTrue(url.endsWith(String.valueOf(gameId)));
	}
}
