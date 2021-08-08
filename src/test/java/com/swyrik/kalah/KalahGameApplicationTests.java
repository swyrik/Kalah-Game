package com.swyrik.kalah;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.swyrik.kalah.entity.Game;
import com.swyrik.kalah.entity.Player;
import com.swyrik.kalah.entity.Status;
import com.swyrik.kalah.repo.KalahRepo;
import com.swyrik.kalah.service.KalahService;

@WebMvcTest
class KalahGameApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	KalahService kalahService;

	@MockBean
	KalahRepo kalahRepo;
	
	@Test
	void testGameCreated() throws Exception {
		Game game = new Game(2L,Status.PROCESSING, new HashMap<>(), Player.PLAYER1, null );
		doReturn(game).when(kalahService).createGame();
		this.mockMvc.perform(post("/games")).andExpect(status().isCreated()).andExpect(jsonPath("$.id").value("2"));
	}
	
	@Test
	void testPlayerInvalidMove() throws Exception {
		MvcResult result = this.mockMvc.perform(put("/games/1/pits/7")).andExpect(status().isBadRequest()).andReturn();
		assertEquals("invalid pit id. please choose a value between 1 and 6", result.getResponse().getContentAsString());
	}
	
	@Test
	void testPlayerMoveGameNotAvailable() throws Exception {
		doReturn(null).when(kalahService).move(2L, 1);
		MvcResult result = this.mockMvc.perform(put("/games/2/pits/1")).andExpect(status().isBadRequest()).andReturn();
		assertEquals("there is no game with the id: " + 2, result.getResponse().getContentAsString());
	}
	
	
	@Test
	void testPlayerMove() throws Exception {
		doReturn(new Game(2L,Status.COMPLETED, new HashMap<Integer, Integer>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{put(1, 0);
		put(2, 7);
		put(3, 7);
		put(4, 7);
		put(5, 7);
		put(6, 7);
		put(7, 1);
		put(8, 6);
		put(9, 6);
		put(10, 6);
		put(11, 6);
		put(12, 6);
		put(13, 6);
		put(14, 1);}}, Player.PLAYER1, "PLAYER1" )).when(kalahService).move(2L, 1);
		this.mockMvc.perform(put("/games/2/pits/1")).andExpect(jsonPath("$.map.2").value(7)).andExpect(jsonPath("$.uri").value("http://localhost:8686/games/2"));
	}

	@Test
	void testGameStatus() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/gamestatus/PLAYER1")).andReturn();
		assertEquals("Game won by " + Player.PLAYER1.toString(), result.getResponse().getContentAsString());
	}

}
