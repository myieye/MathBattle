package com.timhaasdyk.mathbattle.io;

import com.timhaasdyk.mathbattle.models.Player;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public interface OnPlayerAnswerInputListener {
    void onPlayer(Player player, long time);
    boolean onAnswer(String answer, boolean finalAnswer);
    boolean onPlayerAnswer(Player player, String answer, long time, boolean finalAnswer);
}