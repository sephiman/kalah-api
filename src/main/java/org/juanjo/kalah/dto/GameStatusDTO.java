package org.juanjo.kalah.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class GameStatusDTO extends GameDTO {
	private Map<Integer, String> status;
}
