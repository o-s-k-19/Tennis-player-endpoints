package com.tennis.player.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tennis.player.exceptions.CountryAlreadyExistException;
import com.tennis.player.exceptions.CountryNotFoundException;
import com.tennis.player.form.CountryRequest;
import com.tennis.player.model.Country;
import com.tennis.player.repository.CountryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class CountryServiceImpl implements CountryService {

    private CountryRepository countryRepository;

    @Override
    public Country get(long id) throws CountryNotFoundException {
	return countryRepository.findById(id)
		.orElseThrow(() -> new CountryNotFoundException("No country found with id : " + id));
    }

    @Override
    public List<Country> getList() {
	return countryRepository.findAll();
    }

    @Override
    public void delete(long id) throws CountryNotFoundException {
	countryRepository.findById(id)
		.orElseThrow(() -> new CountryNotFoundException("Cannot delete country with id : " + id));
	countryRepository.deleteById(id);
    }

    @Override
    public Country getByCode(String countryCode) {
	return countryRepository.findByCodeIgnoringCase(countryCode);
    }

    @Override
    public Country create(CountryRequest countryRequest) throws CountryAlreadyExistException {
	if (getByCode(countryRequest.getCode()) != null) {
	    throw new CountryAlreadyExistException("Country already exist");
	}
	Country country = new Country();
	BeanUtils.copyProperties(countryRequest, country);
	return countryRepository.save(country);
    }

    @Override
    public Country update(Long id, CountryRequest countryRequest) throws CountryNotFoundException {
	countryRepository.findById(id)
		.orElseThrow(() -> new CountryNotFoundException("No country found with id : " + id));
	Country country = new Country();
	BeanUtils.copyProperties(countryRequest, country);
	country.setId(id);
	return countryRepository.save(country);
    }

}
