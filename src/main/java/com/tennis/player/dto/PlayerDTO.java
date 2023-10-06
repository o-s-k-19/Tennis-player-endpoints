package com.tennis.player.dto;

import org.springframework.hateoas.RepresentationModel;

import com.tennis.player.enums.Gender;
import com.tennis.player.model.PlayerData;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO extends RepresentationModel<PlayerDTO> {

    private Long id;
    private String firstname;
    private String lastname;
    private String shortname;
    private String picture;
    @Enumerated(EnumType.STRING)
    private Gender sex;
    private CountryDTO countryDTO;
    private PlayerData data;
}
