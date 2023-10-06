package com.tennis.player;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
class PlayerRestControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	private static final String PLAYER_ENDPOINT = "/api/v1/players";
	
	@Test
	@DisplayName("doit renvoyer la liste triée des joueur du meilleur au moins bon")
	void shouldGetPlayersRankedList() throws Exception {
	
		mockMvc.perform(get(PLAYER_ENDPOINT))
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
		.andExpect(jsonPath("$.result._embedded.playerDTOList[0].id", is(5)))
		.andExpect(jsonPath("$.result._embedded.playerDTOList[1].id", is(1)))
		.andExpect(jsonPath("$.result._embedded.playerDTOList[2].id", is(4)));
		
	}
	
	@Test
	@DisplayName("doit renvoyer les infos du joueur identifié par 2")
	void shouldGetPlayerById() throws Exception {
	
		mockMvc.perform(get(PLAYER_ENDPOINT+"/2"))
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
		.andExpect(jsonPath("$.result.id", is(2)));
	}
	
	@Test
	@DisplayName("doit renvoyer une reponse d'erreur")
	void shouldGetErrorResponse() throws Exception {
		
		mockMvc.perform(get(PLAYER_ENDPOINT+"/50"))
		.andExpect(status().isNotFound())
		.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
		.andExpect(jsonPath("$.message", is("Not Found")));
	}
	
	@Test
	@DisplayName("doit renvoyer une erreur coté client, requete incorrecte")
	void shouldGetBadRequest() throws Exception {
		
		mockMvc.perform(get(PLAYER_ENDPOINT+"/mm"))
		.andExpect(status().isBadRequest())
		.andExpect(MockMvcResultMatchers.content().contentType("application/json"));
	}
	
	@Test
	@DisplayName("doit renvoyer les statistiques")
	void shouldGetStatistic() throws Exception {
		
		mockMvc.perform(get(PLAYER_ENDPOINT+"/statistics"))
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
		.andExpect(jsonPath("$.result.imcMean", is(23.36)))
		.andExpect(jsonPath("$.result.playerHeightMedian", is(185.0)))
		.andExpect(jsonPath("$.result.countryWithBigRatio.code", is("SRB")))
		.andExpect(jsonPath("$.description", is("Some statistics about tennis sport")));
	}
}
