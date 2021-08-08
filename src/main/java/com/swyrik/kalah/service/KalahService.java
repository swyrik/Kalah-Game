package com.swyrik.kalah.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swyrik.kalah.entity.Game;
import com.swyrik.kalah.entity.Player;
import com.swyrik.kalah.entity.Status;
import com.swyrik.kalah.exceptions.InvalidMoveException;
import com.swyrik.kalah.repo.KalahRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KalahService {
	
	@Autowired
	KalahRepo repo;
	
	public Game createGame() {
		
		Game game = new Game();
		game.setStatus(Status.PROCESSING);
		Map<Integer, Integer> map = new HashMap<>();
			map.put(1,6);map.put(2,6);map.put(3,6);map.put(4,6);map.put(5,6);map.put(6,6);map.put(7,0);
			map.put(8,6);map.put(9,6);map.put(10,6);map.put(11,6);map.put(12,6);map.put(13,6);map.put(14,0);
		
		game.setMap(map);
		game.setPlayerTurn(Player.PLAYER1);
		log.info("game created and saved in the db");
		return repo.save(game);
	}


	public Game move(Long gameId, int pitid) throws InvalidMoveException {
		Optional<Game> gameOpt = repo.findById(gameId);
		if(gameOpt.isPresent()) {
			Game game  = gameOpt.get();
			if(game.getStatus().equals(Status.COMPLETED)){
				return game;
			}
			Map<Integer, Integer> map = game.getMap();
			int leftBound;
			int rightBound;
			Player playerTurn = game.getPlayerTurn();
			if(playerTurn.equals(Player.PLAYER1)) {
				leftBound=1;
				rightBound =6;
			}else {
				leftBound=8;
				rightBound =13;
				pitid = pitid+7;
				
			}
			Integer pitVal = map.get(pitid);
			map.put(pitid, 0);
			if(pitVal==0) {
				throw new InvalidMoveException("there are no stones in the pit");
			}
			while(pitVal>=1) {
				pitid = pitid +1;
				
				if((playerTurn.equals(Player.PLAYER1) &&  pitid == 14) ||  (playerTurn.equals(Player.PLAYER2) &&  pitid == 7)) {
					if(pitid >= 14){
	                	pitid = 0;
	                }
					continue;
				}
				if(((playerTurn.equals(Player.PLAYER1) && pitid == 7) || (playerTurn.equals(Player.PLAYER2) && pitid ==14)) && pitVal==1){
                        map.put(pitid, map.get(pitid) + 1);
                        if(!checkGameOver(map)) {
                        	game.setStatus(Status.COMPLETED);
                        	game = setGameWinner(map, game);
                        	return saveGame(game, map, playerTurn);
                        }
                        return saveGame(game, map, playerTurn);
                }
                oppositePlayerPitCapture(pitid, map, leftBound, rightBound, playerTurn, pitVal);
                if(pitid >= 14){
                	pitid = 0;
                }
                if(!checkGameOver(map)) {
                	game.setStatus(Status.COMPLETED);
                	game = setGameWinner(map, game);
                	return saveGame(game, map, playerTurn);
                }
                pitVal--;
			}
			
			playerTurn = setPlayerTurn(playerTurn);
			return saveGame(game, map, playerTurn);
		}else {
			return null;
		}
		
	}


	private Game setGameWinner(Map<Integer, Integer> map, Game game) {
		if(map.get(7) > map.get(14)) {
			game.setWinner(Player.PLAYER1.toString());
		}else if (map.get(7) < map.get(14)) {
			game.setWinner(Player.PLAYER2.toString());
		}else {
			game.setWinner("MATCH TIED");
		}
		return game;
	}


	private void oppositePlayerPitCapture(int pitid, Map<Integer, Integer> map, int leftBound, int rightBound,
			Player playerTurn, Integer pitVal) {
		if(map.get(pitid) == 0 && pitVal==1 &&( pitid<= rightBound && pitid >= leftBound)){
			int oppPitId = playerTurn.equals(Player.PLAYER1) ? 14-pitid : Math.abs(pitid-14);
			Integer oppPitVal = map.get(oppPitId);
			if(oppPitVal>0) {
				map.put(playerTurn.equals(Player.PLAYER1) ? 7 : 14, 1 + oppPitVal + map.get(playerTurn.equals(Player.PLAYER1) ? 7 : 14));
				map.put(oppPitId, 0);                		
			}else {
				map.put(pitid, map.get(pitid) + 1);
			}
		}else{
		    map.put(pitid, map.get(pitid) + 1);
		}
	}


	private Player setPlayerTurn(Player playerTurn) {
		if(playerTurn.equals(Player.PLAYER1)){
			playerTurn = Player.PLAYER2;
		}else{
			playerTurn = Player.PLAYER1;
		}
		return playerTurn;
	}


	private Game saveGame(Game game, Map<Integer, Integer> map, Player playerTurn) {
		game.setPlayerTurn(playerTurn);
		game.setMap(map);
		repo.save(game);
		return game;
	}
	
	private boolean checkGameOver(Map<Integer, Integer> map) {
		boolean flag = true;
        int sum = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if(entry.getValue() != 0 && entry.getKey()>=1 && entry.getKey()<=6){
                return flag;
            }
            if(entry.getKey()>7 && entry.getKey() < 14){
                sum = sum + entry.getValue();
                entry.setValue(0);
            }else if(entry.getKey() == 14) {
                flag = false;
                map.put(7, entry.getValue()+ sum);
                return flag;
            }
        }
        sum = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if(entry.getKey() < 7){
                sum +=entry.getValue();
            }
            if(entry.getValue() != 0 && entry.getKey()>=8 && entry.getKey()<=14){
                return flag;
            }
        }

        if(flag){
            map.put(14, sum + map.get(7));
            flag = false;
            return flag;
        }

        return flag;
		
	}


	public Map<Integer, Integer> getGameStats(Long id){
		Optional<Game> gameOpt = repo.findById(id);
		if(gameOpt.isPresent()) {
			return gameOpt.get().getMap();
		}else {
			return null;
		}
	}
	
	
}
