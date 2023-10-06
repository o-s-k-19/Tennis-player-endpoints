package com.tennis.player.exceptions;

public class CountryNotFoundException extends Exception {

    private static final long serialVersionUID = -1940715518404734582L;

    public CountryNotFoundException(String msg) {
	super(msg);
    }
}
