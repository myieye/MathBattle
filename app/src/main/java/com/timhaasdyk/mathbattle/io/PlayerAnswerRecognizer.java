package com.timhaasdyk.mathbattle.io;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public interface PlayerAnswerRecognizer extends Recognizer {
    void setOnPlayerAnswerRecognizedListener(OnPlayerAnswerInputListener onPlayerAnswerInputListener);
}