package org.juanjo.kalah.service;

public interface UrlService {
	/**
	 * Generates the URL to access the created game.
	 * I.E. http://localhost:8080/games/123
	 *
	 * @param gameId for the url
	 * @return the url as string
	 */
	String getUrlForGame(long gameId);
}
