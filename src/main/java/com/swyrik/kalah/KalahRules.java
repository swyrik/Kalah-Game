package com.swyrik.kalah;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KalahRules implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		
		log.info("\n\n++++++++++++++++++++++++++++++++++++++\nGAME RULES\n++++++++++++++++++++++++++++++++++++++\n1) At the beginning of the game, six seeds are placed in each house. This is the traditional method.\n"+
"2) Each player controls the six houses and their seeds on the player's side of the board. The player's score is the number of seeds in the store to their right.\n"+
"3) Players take turns sowing their seeds. On a turn, the player removes all seeds from one of the houses under their control. Moving counter-clockwise, the player drops one seed in each house in turn, including the player's own store but not their opponent's.\n"+
"4) If the last sown seed lands in an empty house owned by the player, and the opposite house contains seeds, both the last seed and the opposite seeds are captured and placed into the player's store.\n"+
"5) If the last sown seed lands in the player's store, the player gets an additional move. There is no limit on the number of moves a player can make in their turn.\n"+
"6) When one player no longer has any seeds in any of their houses, the game ends. The other player moves all remaining seeds to their store, and the player with the most seeds in their store wins.\n");

	}

}
