package com.timhaasdyk.mathbattle.io.input;

import android.support.annotation.NonNull;
import com.timhaasdyk.mathbattle.models.Player;

/**
 * @author Tim Haasdyk on 10-May-17.
 */
public class AnsweringPlayer implements Comparable<AnsweringPlayer> {

    private Player player;
    private long answerTime;

    public AnsweringPlayer(Player player, long answerTime) {
        this.player = player;
        this.answerTime = answerTime;
    }

    public Player getPlayer() {
        return player;
    }

    public long getAnswerTime() {
        return answerTime;
    }

    @Override
    public int compareTo(@NonNull AnsweringPlayer otherAnsweringPlayer) {
        return (int)(this.answerTime - otherAnsweringPlayer.getAnswerTime());
    }

    @Override
    public boolean equals(Object obj) {
        AnsweringPlayer that = (obj instanceof AnsweringPlayer) ? (AnsweringPlayer) obj : null;
        return that != null && this.toString().equals(that.toString());
    }

    @Override
    public String toString() {
        return player.getTag().toString() + answerTime;
    }
}