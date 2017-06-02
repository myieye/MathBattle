package com.timhaasdyk.mathbattle.exceptions;

/**
 * @author Tim Haasdyk on 12-May-17.
 */
public class TooManyPlayersException extends RuntimeException {
    public TooManyPlayersException(String msg) {
        super(msg);
    }
}
