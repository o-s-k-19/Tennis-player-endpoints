package com.tennis.player.dto;

import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO extends RepresentationModel<CountryDTO> {

    private long id;
    @NotBlank
    private String picture;
    @NotBlank
    private String code;
}
