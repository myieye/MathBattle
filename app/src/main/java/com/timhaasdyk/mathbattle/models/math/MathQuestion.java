package com.timhaasdyk.mathbattle.models.math;

import com.timhaasdyk.mathbattle.models.Question;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Locale;

/**
 * @author Tim Haasdyk on 06-May-17.
 */
public class MathQuestion extends Question {

    private String speechAnswer;
    private Number numberAnswer;

    public MathQuestion(int level, String text, Number answer, String speechAnswer) {
        super(level, text, String.valueOf(answer));
        this.speechAnswer = speechAnswer;
        numberAnswer = answer;
    }

    @Override
    protected boolean isCorrectAnswer(String attempt) {
        if (StringUtils.isBlank(attempt))
            return false;
        else if (NumberUtils.isCreatable(attempt)) {
            Number numAttempt = NumberUtils.createNumber(attempt);
            return this.numberAnswer.equals(numAttempt);
        } else {
            return StringUtils.equalsIgnoreCase(speechAnswer, attempt);
        }
    }

    @Override
    public String toString() {
        return super.toString() + String.format(Locale.getDefault(), ":{%s}", speechAnswer);
    }
}