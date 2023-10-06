package com.tennis.player.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.RepresentationModel;

import com.tennis.player.dto.CountryDTO;
import com.tennis.player.dto.PlayerDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayersStatistics extends RepresentationModel<PlayersStatistics> {

    private int totalCountries;
    private CountryDTO countryWithBigRatio;
    private long totalPlayers;
    private long numberOfWomen;
    private long numberOfMen;
    private int youngerAge;
    private int AverageAge;
    private int olderAge;
    private PlayerDTO bestPlayer;
    private double imcMean;
    private double playerHeightMedian;
    private Map<String, Integer> numberOfPlayerPerCountry = new HashMap<String, Integer>();

}
