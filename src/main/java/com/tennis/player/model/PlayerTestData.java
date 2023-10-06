package com.tennis.player.model;

import com.tennis.player.enums.Gender;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Utiliser uniquement dans le test 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerTestData {

    private Long id;
    private String firstname;
    private String lastname;
    private String shortname;
    private String picture;
    @Enumerated(EnumType.STRING)
    private Gender sex;
    private Country country;
    private PlayerData data;

}
