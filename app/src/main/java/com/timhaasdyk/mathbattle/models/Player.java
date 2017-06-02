package com.timhaasdyk.mathbattle.models;

/**
 * @author Tim Haasdyk on 06-May-17.
 */
public class Player {

    private String name;
    private PlayerTag tag;
    private int points;

    public Player(PlayerTag tag, String name, int points) {
        this.tag = tag;
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public PlayerTag getTag() {
        return tag;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object obj) {
        Player that = (obj instanceof Player) ? (Player) obj : null;
        return that != null && this.getTag().equals(that.getTag());
    }

    @Override
    public String toString() {
        return name;
    }
}