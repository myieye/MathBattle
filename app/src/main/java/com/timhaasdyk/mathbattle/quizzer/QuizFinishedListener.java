package com.timhaasdyk.mathbattle.quizzer;

import com.timhaasdyk.mathbattle.models.Quiz;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public interface QuizFinishedListener {
    void onQuizFinished(Quiz quiz);
}
