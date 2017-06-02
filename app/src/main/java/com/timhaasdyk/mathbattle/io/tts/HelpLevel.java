package com.timhaasdyk.mathbattle.io.tts;

/**
 * @author Tim Haasdyk on 22-May-17.
 */
public enum HelpLevel { ONE, TWO, THREE;

    public final int I;

    HelpLevel() {
        this.I = this.ordinal();
    }

    public HelpLevel getNextLevel() {
        return this.equals(ONE) ? TWO : THREE;
    }

    public boolean isMax() {
        int n = HelpLevel.values().length;
        return this.equals(HelpLevel.values()[n-1]);
    }
}