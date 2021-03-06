package org.juanjo.kalah.persistence;

import org.juanjo.kalah.dao.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Database repository for games
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
