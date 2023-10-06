package com.tennis.player.controller.rest;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tennis.player.assembler.CountryModelAssembler;
import com.tennis.player.assembler.PlayerModelAssembler;
import com.tennis.player.dto.CountryDTO;
import com.tennis.player.dto.PlayerDTO;
import com.tennis.player.exceptions.CountryAlreadyExistException;
import com.tennis.player.exceptions.CountryNotFoundException;
import com.tennis.player.form.CountryRequest;
import com.tennis.player.model.Country;
import com.tennis.player.model.CustomResponse;
import com.tennis.player.service.CountryService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/countries", produces = "application/hal+json")
@AllArgsConstructor
public class CountryRestController {

    private CountryService countryService;
    private CountryModelAssembler countryModelAssembler;
    private PlayerModelAssembler playerModelAssembler;

    @GetMapping
    public ResponseEntity<CustomResponse<CollectionModel<CountryDTO>>> getAll() {
	CollectionModel<CountryDTO> country = countryModelAssembler.toCollectionModel(countryService.getList());
	HttpStatus httpStatus = HttpStatus.OK;
	CustomResponse<CollectionModel<CountryDTO>> body = new CustomResponse<>(country, "Tennis player country list",
		httpStatus);

	return new ResponseEntity<>(body, httpStatus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<CountryDTO>> getOne(@PathVariable(name = "id", required = true) Long id)
	    throws CountryNotFoundException {
	CountryDTO country = countryModelAssembler.toModel(countryService.get(id));
	HttpStatus httpStatus = HttpStatus.OK;
	CustomResponse<CountryDTO> body = new CustomResponse<>(country, "Country infos", httpStatus);

	return new ResponseEntity<>(body, httpStatus);
    }

    @GetMapping("/{id}/players")
    public ResponseEntity<CustomResponse<CollectionModel<PlayerDTO>>> getPlayers(
	    @PathVariable(name = "id", required = true) Long id) throws CountryNotFoundException {
	Country country = countryService.get(id);
	CollectionModel<PlayerDTO> players = playerModelAssembler.toCollectionModel(country.getPlayers());
	HttpStatus httpStatus = HttpStatus.OK;
	CustomResponse<CollectionModel<PlayerDTO>> body = new CustomResponse<>(players, "List of players in country",
		httpStatus);

	return new ResponseEntity<>(body, httpStatus);
    }

    @PostMapping
    public ResponseEntity<CustomResponse<CountryDTO>> create(@Valid @RequestBody CountryRequest countryRequest)
	    throws CountryAlreadyExistException {
	CountryDTO countryDTO = countryModelAssembler.toModel(countryService.create(countryRequest));
	HttpStatus httpStatus = HttpStatus.CREATED;
	CustomResponse<CountryDTO> body = new CustomResponse<>(countryDTO, "new country infos", httpStatus);

	return new ResponseEntity<>(body, httpStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<CountryDTO>> update(@PathVariable(name = "id", required = true) Long id,
	    @Valid @RequestBody CountryRequest countryRequest) throws CountryNotFoundException {

	CountryDTO countryDTO = countryModelAssembler.toModel(countryService.update(id, countryRequest));
	HttpStatus httpStatus = HttpStatus.OK;
	CustomResponse<CountryDTO> body = new CustomResponse<>(countryDTO, "updated country infos", httpStatus);

	return new ResponseEntity<>(body, httpStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<String>> delete(@PathVariable(name = "id", required = true) Long id)
	    throws CountryNotFoundException {
	countryService.delete(id);
	HttpStatus httpStatus = HttpStatus.OK;
	CustomResponse<String> body = new CustomResponse<>("Deleted country with id : " + id, "delete country", httpStatus);

	return new ResponseEntity<>(body, httpStatus);
    }
}
