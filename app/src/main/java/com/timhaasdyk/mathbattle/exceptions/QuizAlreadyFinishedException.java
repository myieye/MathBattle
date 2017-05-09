package com.timhaasdyk.mathbattle.exceptions;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public class QuizAlreadyFinishedException extends RuntimeException {
    public QuizAlreadyFinishedException(String message) {
        super(message);
    }
}
