package org.juanjo.kalah.dao;

import lombok.Data;
import org.juanjo.kalah.dto.GameStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "game")
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private GameStatus status;
	private Integer firstPlayerScore;
	private Integer secondPlayerScore;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;

	@PrePersist
	private void onCreation() {
		status = GameStatus.IN_PROGRESS;
		createDate = LocalDateTime.now();
		updateDate = createDate;
	}

	@PreUpdate
	private void onUpdate() {
		updateDate = LocalDateTime.now();
	}
}
