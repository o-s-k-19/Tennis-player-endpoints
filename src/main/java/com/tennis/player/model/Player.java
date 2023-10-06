package com.tennis.player.model;

import com.tennis.player.enums.Gender;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String shortname;
    private String picture;
    @Enumerated(EnumType.STRING)
    private Gender sex;
    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE,
	    CascadeType.PERSIST }, optional = false, targetEntity = Country.class)
    private Country country;
    private int rank;
    private int points;
    private int weight;
    private int height;
    private int age;
    @ElementCollection
    @CollectionTable(name = "last_scores", joinColumns = @JoinColumn(name = "player_data_id"))
    @Column(name = "score")
    private int[] last;

}
