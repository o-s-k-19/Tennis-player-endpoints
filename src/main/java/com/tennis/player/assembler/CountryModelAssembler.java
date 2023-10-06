package com.tennis.player.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tennis.player.controller.rest.CountryRestController;
import com.tennis.player.dto.CountryDTO;
import com.tennis.player.exceptions.CountryNotFoundException;
import com.tennis.player.mapper.CountryMapper;
import com.tennis.player.model.Country;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CountryModelAssembler implements RepresentationModelAssembler<Country, CountryDTO> {

    private CountryMapper countryMapper;

    @Override
    public CountryDTO toModel(Country country) {

	try {
	    return countryMapper.mapToCountryDTO(country).add(
		    linkTo(methodOn(CountryRestController.class).getOne(country.getId())).withSelfRel(),
		    linkTo(methodOn(CountryRestController.class).getPlayers(country.getId())).withRel("players"),
		    linkTo(CountryRestController.class).withRel("countries"));
	} catch (CountryNotFoundException e) {
	    e.printStackTrace();
	    return null;
	}
    }
}
