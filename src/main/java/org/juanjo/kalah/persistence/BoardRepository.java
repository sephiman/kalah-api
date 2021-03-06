package org.juanjo.kalah.persistence;

import org.juanjo.kalah.dao.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Database repository for boards
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

	/**
	 * Gets the latest board used given the game id
	 *
	 * @param gameId associated to the board
	 * @return board
	 */
	Board findFirstByGameIdOrderByCreateDateDesc(long gameId);
}
