package com.tennis.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tennis.player.enums.Gender;
import com.tennis.player.exceptions.PlayerNotFoundException;
import com.tennis.player.mapper.CountryMapperImpl;
import com.tennis.player.mapper.PlayerMapperImpl;
import com.tennis.player.model.Country;
import com.tennis.player.model.Player;
import com.tennis.player.model.PlayersStatistics;
import com.tennis.player.repository.CountryRepository;
import com.tennis.player.repository.PlayerRepository;
import com.tennis.player.service.PlayerServiceImpl;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Mock
    private PlayerRepository playerRepository;
    @Mock    
    private CountryRepository countryRepository;
    
    @Spy
    private CountryMapperImpl countryMapper;
    @Spy
    private PlayerMapperImpl playerMapper;
  
    /*
     * Cette methode renvoie les infos d'un joueur
     * 
     * @param id
     * 
     * @return un optional de l'objet Player
     * 
     * @throw IOException, URISyntaxException
     */
    private Optional<Player> getPlayer(long id) throws IOException, URISyntaxException {

	List<Player> players = getPlayerList();

	Optional<Player> optionalPlayer = players.stream().filter(player -> player.getId().equals(id)).findFirst();

	return optionalPlayer;

    }

    private List<Country> getCountryList(){
	return List.of(
		new Country(1l, "country picture 1", "code 1", new ArrayList<Player>()),
		new Country(2l, "country picture 2", "code 2", new ArrayList<Player>()),
		new Country(3l, "country picture 3", "code 3", new ArrayList<Player>()),
		new Country(4l, "country picture 4", "code 4", new ArrayList<Player>()),
		new Country(5l, "country picture 5", "code 5", new ArrayList<Player>())
		);
    }
    
    /*
     * Cette methode Charge les données du test depuis un fichier json
     * 
     * @return un objet PlayersTestData qui contient la liste des joueurs et leur
     * infos
     * 
     * @throw IOException, URISyntaxException
     */
    private List<Player> getPlayerList() {

	List<Player> listOfPlayer = new ArrayList<Player>();
	List<Country> countryList = getCountryList();
	
	for (long i = 0; i < 5; i++) {
	    Player p = new Player();
	    p.setId(i+1);
	    p.setAge((int) i + 27);
	    p.setFirstname("firstname" + i);
	    p.setLastname("lastname" + i);
	    p.setShortname("shortname" + i);
	    p.setSex(Math.random() > 0.5 ? Gender.F : Gender.M);
	    p.setHeight((int) Math.random() * 100 + 100);
	    p.setPicture("player picture" + i);
	    p.setPoints((int) (Math.random() * 1000 + 1000 * i));
	    p.setRank((int) i);
	    p.setWeight((int) Math.random() * 20000 + 50000);
	    p.setCountry(countryList.get((int)i));
	    
	   // countryList.get((int)i).getPlayers().add(p);
	    listOfPlayer.add(p);
	}
	return listOfPlayer;
    }

    @SuppressWarnings("deprecation")
    @BeforeEach
    void init() throws IOException, URISyntaxException {

	this.countryMapper = new CountryMapperImpl();
	this.playerMapper = new PlayerMapperImpl();
	MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Ce test doit permettre de vérifier qu'on recupére une liste triée suivant le rang dans l'ordre ascendant")
    void testShouldGetPlayersListByRank() throws IOException, URISyntaxException {

	// ******************************* GIVEN
	// chargement des données pour le test
	List<Player> players = getPlayerList();
	int page = 0;
	int size = 5;
	int totalPages = 10;
	Direction direction = Direction.ASC;
	String sortField = "rank";
	

	// trie de la liste
	players.sort(new Comparator<Player>() {
	    @Override
	    public int compare(Player p1, Player p2) {
		return p1.getRank() - p2.getRank();
	    }
	});
	Page<Player> playersPage = new PageImpl<>(players, PageRequest.of(page, size), totalPages);
	// ******************************* WHEN
	// Définir le comportement de la methode lors d'un appel
	when(playerRepository.findAll(PageRequest.of(page, size, Sort.by(direction, sortField))))
		.thenReturn(playersPage);

	// Test de playerService en faisant un appel de la methode listByRank
	Page<Player> playersPageResponse = playerService.getList(page, size, direction, sortField);

	List<Player> result = playersPageResponse.getContent();
	// ****************************** THEN
	// pour verifier que l'appel est effectué une seule fois
	verify(playerRepository, times(1)).findAll(PageRequest.of(page, size, Sort.by(direction, sortField)));

	// pour vérifier qu'on a le bon ordre du trie (du meilleur au moins bon)
	assertThat(result.get(0).getRank()).isLessThanOrEqualTo(result.get(1).getRank());
	assertThat(result.get(1).getRank()).isLessThanOrEqualTo(result.get(2).getRank());
    }

    @Test
    @DisplayName("Ce test doit permettre de vérifier qu'on recupere les infos d'un joueur ayant un id valide")
    void testShouldGetPlayerById() throws PlayerNotFoundException, IOException, URISyntaxException {

	// ******************************* GIVEN
	// données du test
	Long playerId = 2L;
	Optional<Player> optionalPlayer = this.getPlayer(playerId);

	// ******************************* WHEN
	// Définir le comportement de la methode lors d'un appel
	when(playerRepository.findById(playerId)).thenReturn(optionalPlayer);

	// excecution du test par l'appel de la méthode
	Player result = playerService.getById(playerId);

	// ******************************* THEN
	// verifier que l'appel est effectué une fois
	verify(playerRepository, times(1)).findById(playerId);

	// vérifier qu'on recupere bien le joueur avec le bon identifiant
	assertThat(playerId).isEqualTo(result.getId());
    }

    @Test
    @DisplayName("Ce test doit permettre de vérifier qu'une exception de type PlayerNotFoundException est lèvée lorsqu'on recupere les infos d'un joueur ayant un id inexistant")
    void testShouldThrowPlayerNotFoundException() throws PlayerNotFoundException, IOException, URISyntaxException {

	// ******************************* GIVEN
	// données du test
	Long playerId = 10L;
	Optional<Player> optionalPlayer = this.getPlayer(playerId);

	// ******************************* WHEN
	// Définir le comportement de la methode lors d'un appel
	when(playerRepository.findById(playerId)).thenReturn(optionalPlayer);

	// ******************************* THEN
	// pour verifier que l'appel à la méthode leve une exception pour un id qui
	// n'existe pas
	assertThatThrownBy(() -> {
	    playerService.getById(playerId);
	}).isInstanceOf(PlayerNotFoundException.class);
    }

    @Test
    @DisplayName("Ce test doit permettre de vérifier le calcul des statistiques ")
    void testShouldRetrieveStatistics() throws IOException, URISyntaxException {

	// ******************************* GIVEN
	// données du test
	List<Player> players = getPlayerList();
	List<Country> countries = getCountryList();

	// ******************************* WHEN
	// Définir le comportement de la methode lors d'un appel
	when(playerRepository.findAll()).thenReturn(players);
	when(countryRepository.findAll()).thenReturn(countries);
	
	PlayersStatistics playersStatistics = playerService.getStatistics();

	// ******************************* THEN
	// pour verifier qu'on a les bon statistics
	assertThat(playersStatistics.getCountryWithBigRatio().getCode()).isEqualTo("code 5");
	assertThat(playersStatistics.getImcMean()).isEqualTo(50.0);
	assertThat(playersStatistics.getPlayerHeightMedian()).isEqualTo(100.0);
    }
}
