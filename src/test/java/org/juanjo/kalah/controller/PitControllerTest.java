package org.juanjo.kalah.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.apache.commons.lang3.RandomUtils;
import org.juanjo.kalah.exception.ValidationException;
import org.juanjo.kalah.service.PitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.verify;


public class PitControllerTest {
	@InjectMocks
	private PitController controller;
	@Mock
	private PitService pitService;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testMoveOK() throws ValidationException {
		long gameId = RandomUtils.nextLong();
		int pitId = RandomUtils.nextInt();
		RestAssuredMockMvc.given().standaloneSetup(controller).when().put("/games/{gameId}/pits/{pitId}", gameId, pitId).then()
				.statusCode(HttpStatus.OK.value());
		verify(pitService).move(gameId, pitId);
	}

}
