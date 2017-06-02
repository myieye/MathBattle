package com.timhaasdyk.mathbattle.models;

import android.support.annotation.NonNull;
import com.timhaasdyk.mathbattle.quizzer.PlayerPosition;

/**
 * @author Tim Haasdyk on 26-May-17.
 */
public class PlayerResult implements Comparable<PlayerResult> {

    private final Player player;
    private final int points;
    private PlayerPosition position;

    public PlayerResult(Player player, int points) {
        this(player, points, null);
    }

    public PlayerResult(Player player, int points, PlayerPosition position) {
        this.player = player;
        this.points = points;
        this.position = position;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public int compareTo(@NonNull PlayerResult playerResult) {
        return playerResult.getPoints() - points;
    }

    public PlayerPosition getPosition() {
        return position;
    }

    public void setPosition(PlayerPosition position) {
        this.position = position;
    }
}
