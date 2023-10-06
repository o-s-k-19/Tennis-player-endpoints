package com.tennis.player.controller.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tennis.player.assembler.CountryModelAssembler;
import com.tennis.player.assembler.PlayerModelAssembler;
import com.tennis.player.dto.CountryDTO;
import com.tennis.player.dto.PlayerDTO;
import com.tennis.player.exceptions.CountryNotFoundException;
import com.tennis.player.exceptions.PlayerNotFoundException;
import com.tennis.player.form.PlayerRequest;
import com.tennis.player.model.CustomResponse;
import com.tennis.player.model.Player;
import com.tennis.player.model.PlayersStatistics;
import com.tennis.player.service.PlayerService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/players", produces = "application/hal+json")
@AllArgsConstructor
public class PlayerRestController {

    private PlayerService playerService;
    private PlayerModelAssembler playerModelAssembler;
    private PagedResourcesAssembler<Player> pagedResourcesAssembler;
    private CountryModelAssembler countryModelAssembler;

    /*
     * Cette méthode renvoie la liste des joueurs triés selon le rang (du meilleur
     * au moins bon)
     */
    @GetMapping
    public ResponseEntity<CustomResponse<PagedModel<PlayerDTO>>> getList(
	    @RequestParam(required = false, name = "page", defaultValue = "0") int page,
	    @RequestParam(required = false, name = "size", defaultValue = "3") int size,
	    @RequestParam(required = false, name = "sortField", defaultValue = "rank") String sortField,
	    @RequestParam(required = false, name = "direction", defaultValue = "ASC") Direction direction
	   ) {

	String description = "List of tennis players sorted by " + sortField + " according to " + direction + " order";
	HttpStatus httpStatus = HttpStatus.OK;
	Page<Player> playerPage = playerService.getList(page, size, direction, sortField);
	PagedModel<PlayerDTO> playerPagedModel = pagedResourcesAssembler.toModel(playerPage, playerModelAssembler);
	CustomResponse<PagedModel<PlayerDTO>> body = new CustomResponse<>(playerPagedModel, description, httpStatus);

	return new ResponseEntity<>(body, httpStatus);

    }

    /**
     * cette méthode renvoie les informations d'un joueur selon son ID ou léve une
     * exception
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<PlayerDTO>> getOne(@PathVariable long id) throws PlayerNotFoundException {
	String description = "Tennis player infos";
	HttpStatus httpStatus = HttpStatus.OK;
	Player player = playerService.getById(id);
	PlayerDTO playerDTO = playerModelAssembler.toModel(player);
	CustomResponse<PlayerDTO> body = new CustomResponse<>(playerDTO, description, httpStatus);

	return new ResponseEntity<>(body, httpStatus);
    }

    /*
     * cette méthode renvoie les informations d'un joueur selon son ID ou léve une
     * exception
     */
    @GetMapping("/{id}/country")
    public ResponseEntity<CustomResponse<CountryDTO>> getCountry(@PathVariable long id) throws PlayerNotFoundException {
	String description = "Tennis player country infos";
	HttpStatus httpStatus = HttpStatus.OK;
	Player player = playerService.getById(id);
	CountryDTO countryDTO = countryModelAssembler.toModel(player.getCountry());
	CustomResponse<CountryDTO> body = new CustomResponse<>(countryDTO, description, httpStatus);

	return new ResponseEntity<>(body, httpStatus);
    }

    /*
     * cette méthode renvoie les informations d'un joueur selon son ID ou léve une
     * exception
     */
    @PostMapping
    public ResponseEntity<CustomResponse<PlayerDTO>> create(@RequestBody @Valid PlayerRequest playerRequest)
	    throws CountryNotFoundException {

	Player savedPlayer = playerService.create(playerRequest);
	PlayerDTO playerDTO = playerModelAssembler.toModel(savedPlayer);
	HttpStatus httpStatus = HttpStatus.CREATED;
	CustomResponse<PlayerDTO> body = new CustomResponse<>(playerDTO, "New player infos", httpStatus);

	return new ResponseEntity<>(body, httpStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<PlayerDTO>> update(@PathVariable long id,
	    @Valid @RequestBody PlayerRequest playerRequest) throws PlayerNotFoundException, CountryNotFoundException {

	Player player = playerService.update(id, playerRequest);
	PlayerDTO playerDTO = playerModelAssembler.toModel(player);
	HttpStatus httpStatus = HttpStatus.OK;
	CustomResponse<PlayerDTO> body = new CustomResponse<>(playerDTO, "Update player infos", httpStatus);
	return new ResponseEntity<>(body, httpStatus);
    }

    /*
     * cette méthode renvoie les informations d'un joueur selon son ID ou léve une
     * exception
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<String>> delete(@PathVariable long id) throws PlayerNotFoundException {
	playerService.delete(id);
	HttpStatus httpStatus = HttpStatus.OK;
	CustomResponse<String> body = new CustomResponse<>("Deleted player with id : " + id, "Detele player", httpStatus);
	return new ResponseEntity<>(body, httpStatus);
    }

    /*
     * Cette méthode renvoie les statistiques (pays ayant le plus ratio, imc moyen,
     * la taille médiane)
     */
    @GetMapping("/statistics")
    public ResponseEntity<CustomResponse<PlayersStatistics>> getStatistics() {
	PlayersStatistics statistics = playerService.getStatistics();
	String description = "Some statistics about tennis sport";
	statistics.add(linkTo(methodOn(PlayerRestController.class).getStatistics()).withSelfRel(),
		linkTo(PlayerRestController.class).withRel("players"),
		linkTo(CountryRestController.class).withRel("countries"));
	HttpStatus httpStatus = HttpStatus.OK;
	CustomResponse<PlayersStatistics> body = new CustomResponse<>(statistics, description, httpStatus);

	return new ResponseEntity<>(body, httpStatus);
    }

}
