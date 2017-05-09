package com.timhaasdyk.mathbattle.models;

import android.graphics.Color;

/**
 * @author Tim Haasdyk on 06-May-17.
 */
public class Player {

    String name;
    Color color;
    int points;

    public Player(String name, Color color, int points) {
        this.name = name;
        this.color = color;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
