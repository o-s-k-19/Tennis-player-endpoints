package com.tennis.player.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tennis.player.model.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {

    Country findByCodeIgnoringCase(String code);

}
