package com.timhaasdyk.mathbattle.exceptions;

/**
 * @author Tim Haasdyk on 12-May-17.
 */
public class DuplicatePlayerException extends RuntimeException {
    public DuplicatePlayerException(String msg) {
        super(msg);
    }
}
