package com.timhaasdyk.mathbattle.logic.impl;

import com.timhaasdyk.mathbattle.logic.QuizProcessor;
import com.timhaasdyk.mathbattle.models.Player;
import com.timhaasdyk.mathbattle.models.PlayerResult;
import com.timhaasdyk.mathbattle.models.Question;
import com.timhaasdyk.mathbattle.models.Quiz;
import com.timhaasdyk.mathbattle.models.PlayerPosition;

import java.util.*;

/**
 * @author Tim Haasdyk on 29-May-17.
 */
public class QuizProcessorImpl implements QuizProcessor {
    @Override
    public Collection<PlayerResult> getQuizResults(Quiz quiz, Collection<Player> players) {
        Map<Player, PlayerResult> resultMap = new HashMap<>();
        Player p;
        for (Question question : quiz.getQuestions()) {
            p = question.getAnsweredBy();
            if (p != null) {
                int prev = resultMap.containsKey(p) ? resultMap.get(p).getPoints() : 0;
                resultMap.put(p, new PlayerResult(p, prev + 1));
            }
        }
        List<PlayerResult> results = new ArrayList<>(resultMap.values());
        Collections.sort(results);

        PlayerPosition lastPlace = PlayerPosition.ONE;
        if (!results.isEmpty()) {
            results.get(0).setPosition(PlayerPosition.ONE);
            for (int i = 1; i < results.size(); i++) {
                PlayerResult a = results.get(i - 1);
                PlayerResult b = results.get(i);
                if (a.getPoints() == b.getPoints()) {
                    b.setPosition(a.getPosition());
                } else {
                    b.setPosition(a.getPosition().next());
                }

                lastPlace = b.getPosition().next();
            }
        }

        for (Player player : players) {
            if (!resultMap.containsKey(player)) {
                results.add(new PlayerResult(player, 0, lastPlace));
            }
        }

        return results;
    }
}