package org.juanjo.kalah.dao;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "board")
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long gameId;
	private int[] pits;
	private Integer pitLastMove;
	private Integer stonesLastMove;
	private LocalDateTime createDate;

	@PrePersist
	protected void onCreation() {
		createDate = LocalDateTime.now();
	}
}
