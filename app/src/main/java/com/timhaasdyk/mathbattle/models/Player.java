package com.timhaasdyk.mathbattle.models;

/**
 * @author Tim Haasdyk on 06-May-17.
 */
public class Player {

    private String name;
    private PlayerTag tag;

    public Player(PlayerTag tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PlayerTag getTag() {
        return tag;
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