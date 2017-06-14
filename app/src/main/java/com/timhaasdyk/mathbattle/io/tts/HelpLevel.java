package com.timhaasdyk.mathbattle.io.tts;

/**
 * @author Tim Haasdyk on 22-May-17.
 */
public enum HelpLevel { ONE, TWO, THREE, LAST;

    public final int I;

    HelpLevel() {
        this.I = this.ordinal();
    }

    public HelpLevel getNextLevel() {
        return HelpLevel.values()[Math.min(HelpLevel.values().length - 1, I + 1)];
    }

    public boolean isMax() {
        return this.equals(LAST);
    }
}