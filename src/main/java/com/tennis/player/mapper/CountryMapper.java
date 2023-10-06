package com.tennis.player.mapper;

import org.mapstruct.Mapper;

import com.tennis.player.dto.CountryDTO;
import com.tennis.player.model.Country;

@Mapper(componentModel = "spring")
public abstract class CountryMapper {

    public abstract CountryDTO mapToCountryDTO(Country contry);

    public abstract Country mapToCountry(CountryDTO countryDTO);

}
