package org.juanjo.kalah.service;

import lombok.extern.slf4j.Slf4j;
import org.juanjo.kalah.exception.FatalErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

@Service
@Slf4j
public class UrlService {
	private final int port;
	private final String serverAddress;
	private static final String PROTOCOL = "http";

	@Autowired
	public UrlService(@Value("${server.port}") int port) throws UnknownHostException {
		this.port = port;
		this.serverAddress = InetAddress.getLocalHost().getHostAddress();
	}

	/**
	 * Generates the URL to access the created game.
	 * I.E. http://localhost:8080/games/123
	 *
	 * @param gameId for the url
	 * @return the url as string
	 */
	public String getUrlForGame(long gameId) {
		try {
			return new URL(PROTOCOL, serverAddress, port, "/games/" + gameId).toExternalForm();
		} catch (MalformedURLException e) {
			log.error("Fail to generate url", e);
			throw new FatalErrorException(e);
		}
	}
}
