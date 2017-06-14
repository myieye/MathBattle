package com.timhaasdyk.mathbattle.logic;

import com.timhaasdyk.mathbattle.models.Quiz;

/**
 * @author Tim Haasdyk on 09-Jun-17.
 */
public interface Quizzer {
    void doQuiz(Quiz quiz);

    void pause();

    void resume();

    void cancel();

    void destroy();
}
