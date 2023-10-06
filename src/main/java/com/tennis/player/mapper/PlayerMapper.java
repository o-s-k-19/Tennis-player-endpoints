package com.tennis.player.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tennis.player.dto.CountryDTO;
import com.tennis.player.dto.PlayerDTO;
import com.tennis.player.model.Country;
import com.tennis.player.model.Player;
import com.tennis.player.model.PlayerData;
import com.tennis.player.repository.CountryRepository;

import lombok.Data;

@Mapper(componentModel = "spring")
@Data
public abstract class PlayerMapper {

    @Autowired
    private CountryRepository countryRepository;

    @Mapping(target = "countryDTO", expression = "java(getCountryDTO(player.getCountry()))")
    @Mapping(target = "data", expression = "java(getData(player))")
    public abstract PlayerDTO mapToPlayerDTO(Player player);

    @Mapping(target = "age", source = "playerDTO.data.age")
    @Mapping(target = "rank", source = "playerDTO.data.rank")
    @Mapping(target = "points", source = "playerDTO.data.points")
    @Mapping(target = "height", source = "playerDTO.data.height")
    @Mapping(target = "weight", source = "playerDTO.data.weight")
    @Mapping(target = "last", source = "playerDTO.data.last")
    @Mapping(target = "country", expression = "java(getCountry(playerDTO.getCountryDTO().getCode()))")
    public abstract Player mapToPlayer(PlayerDTO playerDTO);

    public PlayerData getData(Player player) {
	PlayerData playerData = new PlayerData();
	BeanUtils.copyProperties(player, playerData);
	return playerData;
    }

    public Country getCountry(String code) {
	return countryRepository.findByCodeIgnoringCase(code);
    }

    public CountryDTO getCountryDTO(Country country) {
	CountryDTO countryDTO = new CountryDTO();
	BeanUtils.copyProperties(country, countryDTO);
	return countryDTO;
    }

}
