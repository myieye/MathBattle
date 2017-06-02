package com.timhaasdyk.mathbattle.logic;

import com.timhaasdyk.mathbattle.models.Player;
import com.timhaasdyk.mathbattle.models.PlayerResult;
import com.timhaasdyk.mathbattle.models.Quiz;

import java.util.Collection;

/**
 * @author Tim Haasdyk on 29-May-17.
 */
public interface QuizProcessor {

    Collection<PlayerResult> getQuizResults(Quiz quiz, Collection<Player> players);
}
