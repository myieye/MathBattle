package com.timhaasdyk.mathbattle.exceptions;

/**
 * @author Tim Haasdyk on 11-May-17.
 */
public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String msg) {
        super(msg);
    }
}
