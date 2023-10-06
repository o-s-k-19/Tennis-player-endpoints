package com.tennis.player.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.tennis.player.exceptions.CountryNotFoundException;
import com.tennis.player.exceptions.PlayerNotFoundException;
import com.tennis.player.form.PlayerRequest;
import com.tennis.player.model.Player;
import com.tennis.player.model.PlayersStatistics;

import jakarta.validation.Valid;

public interface PlayerService {

    Page<Player> getList(int page, int size, Direction direction, String sortProperties);

    Player getById(Long id) throws PlayerNotFoundException;

    PlayersStatistics getStatistics();

    Player create(@Valid PlayerRequest playerRequest) throws CountryNotFoundException;

    Player update(long id, @Valid PlayerRequest playerRequest) throws PlayerNotFoundException, CountryNotFoundException;

    void delete(long id) throws PlayerNotFoundException;
}
