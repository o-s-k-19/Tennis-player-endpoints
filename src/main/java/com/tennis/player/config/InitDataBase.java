package com.tennis.player.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tennis.player.model.Country;
import com.tennis.player.model.LoadPlayerTestData;
import com.tennis.player.model.Player;
import com.tennis.player.model.PlayerTestData;
import com.tennis.player.repository.CountryRepository;
import com.tennis.player.repository.PlayerRepository;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class InitDataBase {

    /*
     * Utiliser uniquement dans le cadre du test : Ce bean est utilisé pour inserer
     * des données au niveau de la base de données mémoire h2 lors du demarrage de
     * l'application il utilise pour la desserialisation la classe PlayersTestData
     * 
     */
    @Bean
    CommandLineRunner start(ObjectMapper objectMapper, PlayerRepository playerRepository,
	    CountryRepository countryRepository) {
	return args -> {

	    try (InputStream inputStream = getClass().getResourceAsStream("/headtohead.json");
		    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

		String jsonContent = reader.lines().collect(Collectors.joining(System.lineSeparator()));

		// debug
		// log.info(jsonContent);

		List<PlayerTestData> listOfPlayers = objectMapper.readValue(jsonContent, LoadPlayerTestData.class)
			.getPlayers();

		listOfPlayers.stream().forEach(playerTestData -> {

		    Country country = countryRepository.findByCodeIgnoringCase(playerTestData.getCountry().getCode());

		    if (country == null) {
			country = countryRepository.save(playerTestData.getCountry());
		    }
		    Player player = new Player();
		    BeanUtils.copyProperties(playerTestData, player);
		    BeanUtils.copyProperties(playerTestData.getData(), player);

		    player.setCountry(country);

		    playerRepository.save(player);
		});

	    } catch (JsonProcessingException e) {
		log.info("json exception : verify that data file is valid json");
		e.printStackTrace();
		System.exit(-1);
	    } catch (IOException e1) {
		log.info("IO exception : file is required");
		e1.printStackTrace();
		System.exit(-1);
	    }

	};
    }
}
