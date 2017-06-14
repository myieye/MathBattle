package com.timhaasdyk.mathbattle.logic;

import com.timhaasdyk.mathbattle.models.Player;
import com.timhaasdyk.mathbattle.models.Quiz;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public interface QuizEventsListener {
    void onQuizFinished(Quiz quiz);
    void onPlayerAnsweredQuestion(Player player);
}
