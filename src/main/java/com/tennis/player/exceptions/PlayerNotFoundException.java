package com.tennis.player.exceptions;

public class PlayerNotFoundException extends Exception {

    private static final long serialVersionUID = -1940715518404734582L;

    public PlayerNotFoundException(String msg) {
	super(msg);
    }
}
