package com.timhaasdyk.mathbattle.generators.math;

import com.timhaasdyk.mathbattle.generators.QuestionGenerator;
import com.timhaasdyk.mathbattle.models.Question;
import com.timhaasdyk.mathbattle.models.math.MathQuestion;
import com.udojava.evalex.Expression;

import java.util.Random;

/**
 * @author Tim Haasdyk on 06-May-17.
 */
public class MathQuestionGenerator extends QuestionGenerator {

    private Random rand;
    private String operators = "+-*/";

    public MathQuestionGenerator(int level) {
        this(level, new Random());
    }

    public MathQuestionGenerator(int level, Random rand) {
        super(level);
        this.rand = rand;
    }

    @Override
    public Question generateQuestion() {

        int a = rand.nextInt(30) + 1;
        int b = rand.nextInt(30) + 1;
        int operator = rand.nextInt(4);
        String text = "" + a + operators.charAt(operator) + b;
        int value = new Expression(text).eval().intValue();

        return new MathQuestion(text, value, getLevel());
    }
}
