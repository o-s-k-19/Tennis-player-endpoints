package com.tennis.player.exceptions;

public class CountryAlreadyExistException extends Exception {

    private static final long serialVersionUID = 1L;

    public CountryAlreadyExistException(String msg) {
	super(msg);
    }
}
