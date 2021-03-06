package org.juanjo.kalah.utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Constants for kalah game
 */
@UtilityClass
public final class KalahConstants {
	public static final int INITIAL_HOUSE_STONES = 0;
	public static final int PLAYER_ONE_HOUSE = 6;
	public static final int PLAYER_TWO_HOUSE = 13;
	public static final List<Integer> PLAYER_ONE_PIT_MOVES = Arrays.asList(0, 1, 2, 3, 4, 5);
	public static final List<Integer> PLAYER_TWO_PIT_MOVES = Arrays.asList(7, 8, 9, 10, 11, 12);
	public static final List<Integer> VALID_PIT_MOVEMENTS =
			Stream.concat(PLAYER_ONE_PIT_MOVES.stream(), PLAYER_TWO_PIT_MOVES.stream()).collect(Collectors.toList());
	public static final int BOARD_SIZE = 14;
}
