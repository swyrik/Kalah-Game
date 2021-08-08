package com.swyrik.kalah.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swyrik.kalah.entity.Game;
import com.swyrik.kalah.entity.GameState;
import com.swyrik.kalah.entity.Status;
import com.swyrik.kalah.exceptions.GameNotAvailableException;
import com.swyrik.kalah.exceptions.InvalidMoveException;
import com.swyrik.kalah.exceptions.InvalidPitIdException;
import com.swyrik.kalah.service.KalahService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class KalahController {
	
	@Autowired
	KalahService service;
	
	@Value("${server.port}")
	int port;
	
	@Operation(summary = "To create the game.")
	@ApiResponses(value = {
			@ApiResponse(responseCode="201", description="To create the game and return game id and url.",
					content= {@Content(mediaType = "application/json")})
	})
	@PostMapping("/games")
	public ResponseEntity<Map<String, String>> createGame(){
		Game game = service.createGame();
		Map<String, String> gameStateMap = new HashMap<>();
		gameStateMap.put("id", String.valueOf(game.getId()));
		gameStateMap.put("uri", "http://localhost:"+port+"/games/"+game.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(gameStateMap);
	}
	
	
	@Operation(summary = "Player can move the stones based on the pitid provided.")
	@ApiResponses(value = {
			@ApiResponse(responseCode="200", description="Player can move the stones in a pit by specifying the game id and pit id",
					content= {@Content(mediaType = "application/json")})
	})
	@PutMapping("/games/{gameid}/pits/{pitid}")
	public ResponseEntity<GameState> move(@PathVariable("gameid") Long id, @PathVariable("pitid") int pitid, HttpServletResponse resp) throws InvalidPitIdException, GameNotAvailableException, InvalidMoveException, IOException{
		log.info("move pos : "+ pitid+"\n");
		if(pitid<1 || pitid>6) {
			throw new InvalidPitIdException("invalid pit id. please choose a value between 1 and 6");
		}
		Game game = service.move(id, pitid);
		if(Objects.isNull(game)) {
			throw new GameNotAvailableException("there is no game with the id: " + id);
		}
		if(game.getStatus().equals(Status.COMPLETED)) {
			resp.sendRedirect("/gamestatus/"+ game.getWinner());
		}
		log.info("\nPLayer Turn " + game.getPlayerTurn());
		return ResponseEntity.status(HttpStatus.OK).body(new GameState(game.getId(),"http://localhost:"+port+"/games/"+game.getId(), game.getMap()));
		
	}
	
	
	@Operation(summary = "Useful to visualize the kalah boad on the UI.")
	@ApiResponse(content= {@Content(mediaType = "application/json")})
	@GetMapping("/games/{gameid}")
	@CrossOrigin(origins = "http://localhost:4200")
	public Map<Integer, Integer> getGameMap(@PathVariable("gameid") Long id){
		return service.getGameStats(id);
	}
	
	
	
	@GetMapping("/gamestatus/{winner}")
	public String gameWinner(@PathVariable("winner") String winner){
		return winner.equalsIgnoreCase("MATCH TIED") ? winner : ("Game won by " + winner);
	}
	

}
