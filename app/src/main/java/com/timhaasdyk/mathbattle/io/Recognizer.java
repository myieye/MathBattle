package com.timhaasdyk.mathbattle.io;

/**
 * @author Tim Haasdyk on 02-Jun-17.
 */
public interface Recognizer {
    void startRecognizing();
    void stopRecognizing();
    boolean isReceiving();
    void destroy();
}
