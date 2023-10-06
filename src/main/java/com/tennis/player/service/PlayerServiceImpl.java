package com.tennis.player.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tennis.player.dto.PlayerDTO;
import com.tennis.player.enums.Gender;
import com.tennis.player.exceptions.CountryNotFoundException;
import com.tennis.player.exceptions.PlayerNotFoundException;
import com.tennis.player.form.PlayerRequest;
import com.tennis.player.mapper.CountryMapper;
import com.tennis.player.mapper.PlayerMapper;
import com.tennis.player.model.Country;
import com.tennis.player.model.Player;
import com.tennis.player.model.PlayerData;
import com.tennis.player.model.PlayersStatistics;
import com.tennis.player.repository.CountryRepository;
import com.tennis.player.repository.PlayerRepository;
import com.tennis.player.utilities.PlayerUtilities;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository playerRepository;
    private CountryMapper countryMapper;
    private PlayerMapper playerMapper;
    private CountryRepository countryRepository;

    @Override
    public Page<Player> getList(int page, int size, Direction direction, String sortProperties) {
	log.info("retrieve list of player ordered by rank");
	return playerRepository.findAll(PageRequest.of(page, size, Sort.by(direction, sortProperties)));

    }

    @Override
    public Player getById(Long id) throws PlayerNotFoundException {
	log.info("retrieve player's infos by id");
	return playerRepository.findById(id)
		.orElseThrow(() -> new PlayerNotFoundException("No player found with id " + id));
    }

    @Override
    public PlayersStatistics getStatistics() {
	log.info("get statistics");
	List<Player> players = playerRepository.findAll();
	List<Country> countries = countryRepository.findAll();

	PlayersStatistics playersStatistics = new PlayersStatistics();

	if (!players.isEmpty()) {

	    playersStatistics.setImcMean(PlayerUtilities.getPlayersIMC(players));
	    playersStatistics.setPlayerHeightMedian(PlayerUtilities.getPlayersHeightMedian(players));
	    playersStatistics.setCountryWithBigRatio(
		    countryMapper.mapToCountryDTO(PlayerUtilities.getCountryWithBigRatio(players).get().getKey()));
	    playersStatistics.setTotalPlayers(players.size());
	    playersStatistics
		    .setNumberOfWomen(players.stream().filter(player -> player.getSex().equals(Gender.F)).count());
	    playersStatistics
		    .setNumberOfMen(players.stream().filter(player -> player.getSex().equals(Gender.M)).count());

	    Player bestPlayer = players.stream().max(new Comparator<Player>() {
		@Override
		public int compare(Player p1, Player p2) {
		    return p1.getPoints() - p2.getPoints() + p1.getRank() - p2.getRank();
		}
	    }).get();

	    playersStatistics.setBestPlayer(playerMapper.mapToPlayerDTO(bestPlayer));
	    playersStatistics.setYoungerAge(players.stream().mapToInt(Player::getAge).min().getAsInt());
	    playersStatistics.setOlderAge(players.stream().mapToInt(Player::getAge).max().getAsInt());
	    playersStatistics.setAverageAge((int) players.stream().mapToInt(Player::getAge).average().getAsDouble());
	}

	if (!countries.isEmpty()) {
	    playersStatistics.setTotalCountries(countries.size());
	    countries.stream().forEach(country -> {
		playersStatistics.getNumberOfPlayerPerCountry().put(country.getCode(), country.getPlayers().size());
	    });

	}
	return playersStatistics;
    }

    @Override
    public Player create(PlayerRequest playerRequest) throws CountryNotFoundException {
	PlayerDTO playerDTO = new PlayerDTO();
	PlayerData playerData = new PlayerData();
	BeanUtils.copyProperties(playerRequest, playerDTO);
	BeanUtils.copyProperties(playerRequest, playerData);
	playerDTO.setData(playerData);
	Player player = playerMapper.mapToPlayer(playerDTO);

	if (player.getCountry() == null) {
	    throw new CountryNotFoundException("Invalide country code : " + playerRequest.getCountryCode());
	}
	return playerRepository.save(player);
    }

    @Override
    public Player update(long id, PlayerRequest playerRequest)
	    throws PlayerNotFoundException, CountryNotFoundException {
	playerRepository.findById(id)
		.orElseThrow(() -> new PlayerNotFoundException("Could not update player with id : " + id));
	PlayerDTO playerDTO = new PlayerDTO();
	PlayerData playerData = new PlayerData();
	BeanUtils.copyProperties(playerRequest, playerDTO);
	BeanUtils.copyProperties(playerRequest, playerData);
	playerDTO.setData(playerData);
	Player newPlayer = playerMapper.mapToPlayer(playerDTO);

	if (newPlayer.getCountry() == null) {
	    throw new CountryNotFoundException("Invalide country code : " + playerRequest.getCountryCode());
	}

	newPlayer.setId(id);
	return playerRepository.save(newPlayer);
    }

    @Override
    public void delete(long id) throws PlayerNotFoundException {
	playerRepository.findById(id)
		.orElseThrow(() -> new PlayerNotFoundException("Cannot delete player with id " + id));
	playerRepository.deleteById(id);
    }

}
