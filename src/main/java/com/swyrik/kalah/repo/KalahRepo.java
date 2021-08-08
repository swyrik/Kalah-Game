package com.swyrik.kalah.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swyrik.kalah.entity.Game;

@Repository
public interface KalahRepo extends JpaRepository<Game, Long> {

}
