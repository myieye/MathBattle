package com.timhaasdyk.mathbattle.generators;

import com.timhaasdyk.mathbattle.models.Question;

/**
 * @author Tim Haasdyk on 09-May-17.
 */
public abstract class QuestionGenerator {

    private int level;

    public QuestionGenerator(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public abstract Question generateQuestion();
}