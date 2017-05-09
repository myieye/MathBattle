package com.timhaasdyk.mathbattle.models.math;

import com.timhaasdyk.mathbattle.models.Question;

/**
 * @author Tim Haasdyk on 06-May-17.
 */
public class MathQuestion extends Question {

    public MathQuestion(String text, int answer, int level) {
        super(text, String.valueOf(answer), level);
    }


    @Override
    protected boolean isCorrectAnswer(String attempt) {
        Integer intAttempt = tryIntParse(attempt);
        return intAttempt != null && getIntAnswer().equals(intAttempt);
    }

    private Integer tryIntParse(String intString) {
        try {
            return Integer.parseInt(intString);
        } catch(NumberFormatException e) {
            return null;
        }
    }

    private Integer getIntAnswer() {
        return Integer.parseInt(getAnswer());
    }
}