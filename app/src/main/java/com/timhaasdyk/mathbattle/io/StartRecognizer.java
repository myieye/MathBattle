package com.timhaasdyk.mathbattle.io;

/**
 * @author Tim Haasdyk on 02-Jun-17.
 */
public interface StartRecognizer extends Recognizer {
    void setOnStartRecognizedListener(OnStartListener onStartListener);
}
