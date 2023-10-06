package com.tennis.player.form;

import com.tennis.player.enums.Gender;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlayerRequest {

    @NotBlank
    private String lastname;
    @NotBlank
    private String firstname;
    @NotBlank
    private String shortname;
    @NotBlank
    private String picture;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender sex;
    @NotBlank
    private String countryCode;
    @Positive
    private int rank;
    @Positive
    private int points = 0;
    @Positive
    private int weight;
    @Positive
    private int height;
    @Min(value = 16)
    private int age;
    private int[] last = { 0, 0, 0, 0 };
}
