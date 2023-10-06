package com.tennis.player.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tennis.player.model.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

}
