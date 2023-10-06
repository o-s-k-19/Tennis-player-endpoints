package com.tennis.player.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tennis.player.controller.rest.PlayerRestController;
import com.tennis.player.dto.PlayerDTO;
import com.tennis.player.exceptions.PlayerNotFoundException;
import com.tennis.player.mapper.PlayerMapper;
import com.tennis.player.model.Player;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PlayerModelAssembler implements RepresentationModelAssembler<Player, PlayerDTO> {

    private PlayerMapper playerMapper;

    @Override
    public PlayerDTO toModel(Player player) {

	try {
	    return playerMapper.mapToPlayerDTO(player).add(
		    linkTo(methodOn(PlayerRestController.class).getOne(player.getId())).withSelfRel(),
		    linkTo(methodOn(PlayerRestController.class).getCountry(player.getId())).withRel("country"),
		    linkTo(PlayerRestController.class).withRel("players"));
	} catch (PlayerNotFoundException e) {
	    e.printStackTrace();
	}
	return null;
    }
}
