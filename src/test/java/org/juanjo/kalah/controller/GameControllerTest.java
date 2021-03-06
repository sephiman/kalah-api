package org.juanjo.kalah.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.juanjo.kalah.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.verify;


public class GameControllerTest {
	@InjectMocks
	private GameController controller;
	@Mock
	private GameService gameService;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateKalahOK() {
		RestAssuredMockMvc.given().standaloneSetup(controller).when().post("/games").then().statusCode(HttpStatus.CREATED.value());
		verify(gameService).createKalahGame();
	}

}
