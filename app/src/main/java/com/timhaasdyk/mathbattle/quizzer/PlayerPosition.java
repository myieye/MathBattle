package com.timhaasdyk.mathbattle.quizzer;

/**
 * @author Tim Haasdyk on 12-May-17.
 */
public enum PlayerPosition {
    ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT;
    public final int I = ordinal();
    public final int N = ordinal() + 1;

    public PlayerPosition add(int add) {
        return PlayerPosition.values()[I + add];
    }

    public PlayerPosition next() {
        return I < PlayerPosition.values().length - 1
                ? PlayerPosition.values()[I + 1]
                : PlayerPosition.values()[I];
    }
}
