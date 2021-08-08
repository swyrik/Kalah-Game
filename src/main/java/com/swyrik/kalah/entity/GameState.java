package com.swyrik.kalah.entity;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameState {
	
	long id;
	String uri;
	Map<Integer, Integer> map;
	
	
	public GameState(long id, String uri) {
		super();
		this.id = id;
		this.uri = uri;
	}

}
