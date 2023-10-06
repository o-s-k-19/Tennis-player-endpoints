package com.tennis.player.service;

import java.util.List;

import com.tennis.player.exceptions.CountryAlreadyExistException;
import com.tennis.player.exceptions.CountryNotFoundException;
import com.tennis.player.form.CountryRequest;
import com.tennis.player.model.Country;

import jakarta.validation.Valid;

public interface CountryService {

    Country get(long id) throws CountryNotFoundException;

    List<Country> getList();

    void delete(long id) throws CountryNotFoundException;

    Country getByCode(String countryCode);

    Country create(@Valid CountryRequest countryRequest) throws CountryAlreadyExistException;

    Country update(Long id, @Valid CountryRequest countryRequest) throws CountryNotFoundException;
}
